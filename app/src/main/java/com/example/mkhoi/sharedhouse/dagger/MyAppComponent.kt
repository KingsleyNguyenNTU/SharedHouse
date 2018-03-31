package com.example.mkhoi.sharedhouse.dagger

import com.example.mkhoi.sharedhouse.MyApp
import com.example.mkhoi.sharedhouse.backup.BackupRepository
import com.example.mkhoi.sharedhouse.backup.RestoreRepository
import com.example.mkhoi.sharedhouse.fee_edit.EditFeeRepository
import com.example.mkhoi.sharedhouse.fees_view.FeesRepository
import com.example.mkhoi.sharedhouse.monthly_bill.MonthlyBillRepository
import com.example.mkhoi.sharedhouse.room_edit.EditRoomRepository
import com.example.mkhoi.sharedhouse.rooms_view.RoomsRepository
import com.example.mkhoi.sharedhouse.settings.SettingsRepository
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface MyAppComponent {
    fun inject(app: MyApp)

    fun editRoomRepository(): EditRoomRepository
    fun roomsRepository(): RoomsRepository
    fun editFeeRepository(): EditFeeRepository
    fun feesRepository(): FeesRepository
    fun monthlyBillRepository(): MonthlyBillRepository
    fun backupRepository(): BackupRepository
    fun restoreRepository(): RestoreRepository
    fun settingsRepository(): SettingsRepository
}