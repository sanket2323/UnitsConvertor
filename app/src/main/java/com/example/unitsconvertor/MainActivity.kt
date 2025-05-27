package com.example.unitsconvertor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.unitsconvertor.ui.theme.UnitsConvertorTheme
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UnitsConvertorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    UnitConvertor()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnitConvertor() {
    var inputValue by remember { mutableStateOf("") }
    var outputValue by remember { mutableStateOf("") }
    var inputUnit by remember { mutableStateOf("Select Unit") }
    var outputUnit by remember { mutableStateOf("Select Unit") }
    var iExpanded by remember { mutableStateOf(false) }
    var oExpanded by remember { mutableStateOf(false) }

    val conversionFactor = remember { mutableStateOf(1.00) }
    val oconversionFactor = remember { mutableStateOf(1.00) }
    var resultUnit by remember { mutableStateOf("") }

    fun convertUnit() {
        val inputValueDouble = inputValue.toDoubleOrNull() ?: 0.0
        if (inputValueDouble == 0.0 && inputValue.isNotEmpty()) {
            outputValue = "Invalid input"
            return
        }
        val result = inputValueDouble * conversionFactor.value
        val finalResult = (result * oconversionFactor.value)
        outputValue = if (finalResult == finalResult.roundToInt().toDouble()) {
            finalResult.roundToInt().toString()
        } else {
            String.format("%.3f", finalResult)
        }
    }

    // Auto-convert when input changes
    LaunchedEffect(inputValue, conversionFactor.value, oconversionFactor.value) {
        if (inputValue.isNotEmpty() && inputUnit != "Select Unit" && outputUnit != "Select Unit") {
            convertUnit()
        } else {
            outputValue = ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                        MaterialTheme.colorScheme.background
                    )
                )
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Text(
                text = "Units Converter",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp
                ),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                textAlign = TextAlign.Center
            )
        }

        // Input Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "From",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = inputValue,
                    onValueChange = { inputValue = it },
                    label = { Text("Enter value") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    singleLine = true
                )

                Box {
                    Button(
                        onClick = { iExpanded = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (inputUnit == "Select Unit")
                                MaterialTheme.colorScheme.outline
                            else MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            text = inputUnit,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Start
                        )
                        Icon(
                            Icons.Default.ArrowDropDown,
                            contentDescription = "Select input unit"
                        )
                    }

                    DropdownMenu(
                        expanded = iExpanded,
                        onDismissRequest = { iExpanded = false }
                    ) {
                        listOf(
                            "Centimeters" to 0.01,
                            "Meters" to 1.0,
                            "Feet" to 0.3048,
                            "Millimeters" to 0.001,
                            "Inches" to 0.0254,
                            "Kilometers" to 1000.0
                        ).forEach { (unit, factor) ->
                            DropdownMenuItem(
                                text = { Text(unit) },
                                onClick = {
                                    iExpanded = false
                                    inputUnit = unit
                                    conversionFactor.value = factor
                                }
                            )
                        }
                    }
                }
            }
        }

        // Swap Button
        FloatingActionButton(
            onClick = {
                val tempUnit = inputUnit
                val tempFactor = conversionFactor.value
                inputUnit = outputUnit
                conversionFactor.value = oconversionFactor.value
                outputUnit = tempUnit
                oconversionFactor.value = tempFactor
                resultUnit = if (inputUnit != "Select Unit") inputUnit else ""
            },
            modifier = Modifier.padding(vertical = 8.dp),
            containerColor = MaterialTheme.colorScheme.secondary
        ) {
            Icon(
                Icons.Default.ArrowDropDown,
                contentDescription = "Swap units",
                tint = MaterialTheme.colorScheme.onSecondary
            )
        }

        // Output Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "To",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Box {
                    Button(
                        onClick = { oExpanded = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (outputUnit == "Select Unit")
                                MaterialTheme.colorScheme.outline
                            else MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            text = outputUnit,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Start
                        )
                        Icon(
                            Icons.Default.ArrowDropDown,
                            contentDescription = "Select output unit"
                        )
                    }

                    DropdownMenu(
                        expanded = oExpanded,
                        onDismissRequest = { oExpanded = false }
                    ) {
                        listOf(
                            "Centimeters" to 100.0,
                            "Meters" to 1.0,
                            "Feet" to 3.28084,
                            "Millimeters" to 1000.0,
                            "Inches" to 39.3701,
                            "Kilometers" to 0.001
                        ).forEach { (unit, factor) ->
                            DropdownMenuItem(
                                text = { Text(unit) },
                                onClick = {
                                    resultUnit = unit
                                    oExpanded = false
                                    outputUnit = unit
                                    oconversionFactor.value = factor
                                }
                            )
                        }
                    }
                }
            }
        }

        // Result Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Result",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                if (outputValue.isNotEmpty() && outputValue != "Invalid input") {
                    Text(
                        text = "$outputValue $resultUnit",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        ),
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        textAlign = TextAlign.Center
                    )
                } else if (outputValue == "Invalid input") {
                    Text(
                        text = "Please enter a valid number",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                } else {
                    Text(
                        text = "Select units and enter a value",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.6f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Quick conversion info
        if (inputValue.isNotEmpty() && inputUnit != "Select Unit" && outputUnit != "Select Unit") {
            Text(
                text = "1 $inputUnit = ${String.format("%.4f", oconversionFactor.value / conversionFactor.value)} $resultUnit",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                modifier = Modifier.padding(top = 16.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewFunction() {
    UnitsConvertorTheme {
        UnitConvertor()
    }
}