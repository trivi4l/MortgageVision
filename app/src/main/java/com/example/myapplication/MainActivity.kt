package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MortgageScreen()
        }
    }
}

@Composable
fun MortgageScreen() {

    var apartmentPrice by remember { mutableStateOf("") }
    var downPayment by remember { mutableStateOf("") }
    var rate by remember { mutableStateOf("") }
    var years by remember { mutableStateOf("") }

    var monthlyResult by remember { mutableStateOf("") }
    var totalResult by remember { mutableStateOf("") }
    var overpaymentResult by remember { mutableStateOf("") }
    var compareResult by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "🏠 Ипотека PRO",
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Узнайте реальную стоимость ипотеки и размер переплаты банку"
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = apartmentPrice,
            onValueChange = { apartmentPrice = it },
            label = { Text("Стоимость квартиры (₽)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = downPayment,
            onValueChange = { downPayment = it },
            label = { Text("Первоначальный взнос (₽)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = rate,
            onValueChange = { rate = it },
            label = { Text("Ставка (%)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = years,
            onValueChange = { years = it },
            label = { Text("Срок (лет)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {

                try {

                    val price = apartmentPrice.toDouble()
                    val down = downPayment.toDouble()
                    val interest = rate.toDouble()
                    val period = years.toInt()

                    val loanAmount = price - down

                    val monthly =
                        MortgageCalculator.monthlyPayment(
                            loanAmount,
                            interest,
                            period
                        )

                    val total =
                        MortgageCalculator.totalPayment(
                            monthly,
                            period
                        )

                    val over =
                        MortgageCalculator.overpayment(
                            total,
                            loanAmount
                        )

                    monthlyResult =
                        "${"%,.0f".format(monthly)} ₽"

                    totalResult =
                        "${"%,.0f".format(total)} ₽"

                    overpaymentResult =
                        "${"%,.0f".format(over)} ₽"

                    if (period > 5) {

                        val shorterPeriod = period - 5

                        val shorterMonthly =
                            MortgageCalculator.monthlyPayment(
                                loanAmount,
                                interest,
                                shorterPeriod
                            )

                        val shorterTotal =
                            MortgageCalculator.totalPayment(
                                shorterMonthly,
                                shorterPeriod
                            )

                        val economy =
                            total - shorterTotal

                        compareResult =
                            """
Срок: $shorterPeriod лет

Экономия:
${"%,.0f".format(economy)} ₽

Платеж:
${"%,.0f".format(shorterMonthly)} ₽
                            """.trimIndent()
                    }

                } catch (e: Exception) {

                    monthlyResult = "Проверьте введённые данные"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Рассчитать ипотеку")
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (monthlyResult.isNotEmpty()) {

            ResultCard(
                "Ежемесячный платеж",
                monthlyResult
            )

            Spacer(modifier = Modifier.height(12.dp))

            ResultCard(
                "Общая сумма выплат",
                totalResult
            )

            Spacer(modifier = Modifier.height(12.dp))

            ResultCard(
                "Переплата банку",
                overpaymentResult
            )

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = "💡 Сколько можно сэкономить",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(compareResult)
                }
            }
        }
    }
}

@Composable
fun ResultCard(
    title: String,
    value: String
) {

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}