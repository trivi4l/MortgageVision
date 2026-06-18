package com.example.myapplication

import kotlin.math.pow

object MortgageCalculator {

    fun monthlyPayment(
        loanAmount: Double,
        annualRate: Double,
        years: Int
    ): Double {

        val monthlyRate = annualRate / 100 / 12
        val months = years * 12

        return loanAmount *
                (monthlyRate * (1 + monthlyRate).pow(months)) /
                ((1 + monthlyRate).pow(months) - 1)
    }

    fun totalPayment(
        monthlyPayment: Double,
        years: Int
    ): Double {
        return monthlyPayment * years * 12
    }

    fun overpayment(
        totalPayment: Double,
        loanAmount: Double
    ): Double {
        return totalPayment - loanAmount
    }
}