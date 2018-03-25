package com.example.mkhoi.sharedhouse.monthly_bill

import android.arch.lifecycle.MutableLiveData
import android.content.Context
import com.example.mkhoi.sharedhouse.database.bean.FeeWithSplitters
import com.example.mkhoi.sharedhouse.database.bean.UnitWithPersons
import com.example.mkhoi.sharedhouse.database.dao.PersonDao
import com.example.mkhoi.sharedhouse.database.dao.SplitterDao
import com.example.mkhoi.sharedhouse.database.dao.UnitPersonDao
import com.example.mkhoi.sharedhouse.database.entity.Fee
import com.example.mkhoi.sharedhouse.database.entity.Person
import com.example.mkhoi.sharedhouse.list_view.BillDetailListItem
import com.example.mkhoi.sharedhouse.list_view.BillListItem
import com.example.mkhoi.sharedhouse.list_view.BillRoommateListItem
import com.example.mkhoi.sharedhouse.util.getProfilePicture
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MonthlyBillRepository @Inject constructor(private val splitterDao: SplitterDao,
                                                private val unitPersonDao: UnitPersonDao,
                                                private val personDao: PersonDao,
                                                private val context: Context) {
    fun getMonthlyBills(selectedMonth: Calendar, resultLiveData: MutableLiveData<List<BillListItem>>) {
        val billItemToRoomId: MutableMap<Int, BillListItem> = mutableMapOf()

        //load required data from DB
        val feesWithSplitters = splitterDao.getAllFeesFromMonthStatic(
                selectedMonth.get(Calendar.MONTH),
                selectedMonth.get(Calendar.YEAR))

        val rooms = unitPersonDao.getAllUnits().associateBy({it.unit.id}, {it})
        val persons = personDao.getAllPersons().associateBy({it.id}, {it})

        for (feeWithSplitters in feesWithSplitters){
            val (roomSplitFees: MutableMap<Int, Float>, personSplitFees: MutableMap<Int, Float>) =
                    createListFeeShareByRoomAndByPerson(feeWithSplitters, rooms, persons)

            prepareBillItems(roomSplitFees, rooms, billItemToRoomId, feeWithSplitters, personSplitFees)
        }

        resultLiveData.postValue(billItemToRoomId.values.toList())
    }

    /**
     * For each roomWithRoommates sharing the current fee, combine the fee amount into the total fee amount
     * of this roomWithRoommates for the current month.
     *
     * Create a bill detail item for the sharing amount of this fee shared by this roomWithRoommates,
     * include this into list of bill details of this roomWithRoommates
     */
    private fun prepareBillItems(roomSplitFees: MutableMap<Int, Float>,
                                 rooms: Map<Int?, UnitWithPersons>,
                                 billItemToRoomId: MutableMap<Int, BillListItem>,
                                 feeWithSplitters: FeeWithSplitters,
                                 personSplitFees: MutableMap<Int, Float>) {
        for (roomSplitFee in roomSplitFees) {
            val roomId = roomSplitFee.key
            val amount = roomSplitFee.value

            rooms[roomId]?.unit?.let { room ->
                val billItem = billItemToRoomId[roomId]
                        ?: BillListItem(
                                mainName = room.name,
                                amount = 0f,
                                phoneNumbers = rooms[roomId]!!.roommates?.map { it.phone.removePrefix("+") }
                                        ?: emptyList(),
                                profilePicture = rooms[roomId]!!.getProfilePicture(context),
                                billDetails = mutableListOf()
                        )
                billItem.amount += amount
                prepareBillDetails(
                        billItem.billDetails,
                        feeWithSplitters.fee,
                        amount,
                        rooms[roomId]!!.roommates,
                        personSplitFees)

                billItemToRoomId.put(roomId, billItem)
            }
        }
    }

    /**
     * Create 2 lists for the current fee:
     *  + List of person sharing this fee
     *  + List of roomWithRoommates sharing this fee
     *
     * In case this fee is shared by roomWithRoommates, for each roomWithRoommates sharing this fee:
     * include all roommates in this roomWithRoommates into list of person sharing this fee,
     * each roommate in same roomWithRoommates will share the fee equally
     *
     * In case this fee is shared by people, group all people shared same roomWithRoommates
     * to get the fee shared by roomWithRoommates, then add it into list of roomWithRoommates sharing this fee
     *
     */
    private fun createListFeeShareByRoomAndByPerson(feeWithSplitters: FeeWithSplitters,
                                                    rooms: Map<Int?, UnitWithPersons>,
                                                    persons: Map<Int?, Person>):
            Pair<MutableMap<Int, Float>, MutableMap<Int, Float>> {
        val roomSplitFees: MutableMap<Int, Float> = mutableMapOf()
        val personSplitFees: MutableMap<Int, Float> = mutableMapOf()

        feeWithSplitters.splitters?.let {
            val totalShare = it.sumBy { it.share }.toFloat()
            for (feeShare in it) {
                val amount = feeShare.share.toFloat().div(totalShare) * feeWithSplitters.fee.amount
                if (feeWithSplitters.fee.isSharedByRoom()) {
                    roomSplitFees.put(feeShare.unitId, amount.toFloat())
                    rooms[feeShare.unitId]?.apply {
                        val roommateShareAmount = amount.div(roommates?.size ?: 1)
                        roommates?.forEach {
                            personSplitFees.put(it.id!!, roommateShareAmount.toFloat())
                        }
                    }
                } else {
                    personSplitFees.put(feeShare.personId, amount.toFloat())
                    persons[feeShare.personId]?.let {
                        val roomId = it.unitId
                        val roomSplitFee = amount.toFloat() + (roomSplitFees[roomId] ?: 0f)
                        roomSplitFees.put(roomId, roomSplitFee)
                    }
                }
            }
        }
        return Pair(roomSplitFees, personSplitFees)
    }

    /**
     * Create an bill detail item for the current roomWithRoommates and the current roomWithRoommates
     * then add this new item into list of bill details of the current roomWithRoommates
     */
    private fun prepareBillDetails(billDetails: MutableList<BillDetailListItem>,
                                   fee: Fee,
                                   amount: Float,
                                   roommates: List<Person>?,
                                   personSplitFees: MutableMap<Int, Float>) {
        val newItem = BillDetailListItem(
                mainName = fee.name,
                amount = amount,
                roommates = prepareBillRoommates(roommates, personSplitFees)
        )
        billDetails.add(newItem)
    }

    /**
     * Create a list of roommates sharing the current fee in the current roomWithRoommates
     */
    private fun prepareBillRoommates(roommates: List<Person>?,
                                     personSplitFees: MutableMap<Int, Float>)
            : MutableList<BillRoommateListItem> {
        val roommateSplitFee : MutableList<BillRoommateListItem> = mutableListOf()
        roommates?.forEach { roommate ->
            personSplitFees[roommate.id]?.let {
                roommateSplitFee.add(BillRoommateListItem(
                        mainName = roommate.name,
                        amount = it
                ))
            }
        }
        return roommateSplitFee
    }
}