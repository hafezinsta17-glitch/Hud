package com.example.data

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

enum class SearchFilter(val label: String, val code: String) {
    ALL("ALL FIELDS", "ALL"),
    NATIONAL_CODE("NAT. CODE", "NAT"),
    MOBILE("MOBILE", "MOB"),
    CARD_NO("CARD NO", "CRD"),
    ACCOUNT("ACCOUNT", "ACC"),
    NAME("NAME", "NAM"),
    CITY("CITY", "CTY")
}

class RecordRepository(
    private val recordDao: RecordDao,
    private val csvImporter: CsvImporter
) {

    fun getRecordCount(): Flow<Int> = recordDao.getRecordCount()

    fun searchRecords(query: String, filter: SearchFilter): Flow<List<RecordEntity>> {
        val cleanQuery = query.trim()
        if (cleanQuery.isEmpty()) {
            return recordDao.getAllRecords(limit = 100)
        }
        return when (filter) {
            SearchFilter.ALL -> recordDao.searchAllFields(cleanQuery)
            SearchFilter.NATIONAL_CODE -> recordDao.searchByNationalCode(cleanQuery)
            SearchFilter.MOBILE -> recordDao.searchByMobile(cleanQuery)
            SearchFilter.CARD_NO -> recordDao.searchByCardNo(cleanQuery)
            SearchFilter.ACCOUNT -> recordDao.searchByAccount(cleanQuery)
            SearchFilter.NAME -> recordDao.searchByName(cleanQuery)
            SearchFilter.CITY -> recordDao.searchByCity(cleanQuery)
        }
    }

    fun getRecordById(id: Long): Flow<RecordEntity?> = recordDao.getRecordById(id)

    suspend fun importCsvUri(context: Context, uri: Uri): Flow<ImportProgress> {
        return csvImporter.importFromUri(context, uri)
    }

    suspend fun loadSampleRecords(): Int {
        val samples = SampleDataProvider.getSampleRecords()
        recordDao.insertBatch(samples)
        return samples.size
    }

    suspend fun deleteRecord(id: Long) = recordDao.deleteRecordById(id)

    suspend fun clearDatabase(): Int = recordDao.deleteAllRecords()

    suspend fun exportCsv(): String = csvImporter.exportToCsvString()
}
