package com.example.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "records",
    indices = [
        Index(value = ["national_code"]),
        Index(value = ["account_number"]),
        Index(value = ["mobile"]),
        Index(value = ["card_no"]),
        Index(value = ["full_name"]),
        Index(value = ["id_number"]),
        Index(value = ["city_name"])
    ]
)
data class RecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "national_code")
    val nationalCode: String = "",

    @ColumnInfo(name = "account_number")
    val accountNumber: String = "",

    @ColumnInfo(name = "full_name")
    val fullName: String = "",

    @ColumnInfo(name = "father_name")
    val fatherName: String = "",

    @ColumnInfo(name = "id_number")
    val idNumber: String = "",

    @ColumnInfo(name = "birth_date")
    val birthDate: String = "",

    @ColumnInfo(name = "city_name")
    val cityName: String = "",

    @ColumnInfo(name = "province_name")
    val provinceName: String = "",

    @ColumnInfo(name = "birth_city")
    val birthCity: String = "",

    @ColumnInfo(name = "birth_province")
    val birthProvince: String = "",

    @ColumnInfo(name = "address")
    val address: String = "",

    @ColumnInfo(name = "card_no")
    val cardNo: String = "",

    @ColumnInfo(name = "mobile")
    val mobile: String = "",

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
) {
    /**
     * Formats 16-digit card number with standard quad spacing (e.g., 6037 9918 1234 5678)
     */
    val formattedCardNumber: String
        get() {
            val digits = cardNo.filter { it.isDigit() }
            return if (digits.length == 16) {
                digits.chunked(4).joinToString(" ")
            } else {
                cardNo
            }
        }

    /**
     * Formats mobile number (e.g. 0912 345 6789)
     */
    val formattedMobile: String
        get() {
            val digits = mobile.filter { it.isDigit() }
            return if (digits.length == 11 && digits.startsWith("0")) {
                "${digits.substring(0, 4)} ${digits.substring(4, 7)} ${digits.substring(7)}"
            } else {
                mobile
            }
        }
}
