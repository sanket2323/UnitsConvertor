package com.example.unitsconvertor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
                    unitConvertor()
                }
            }
        }
    }
}


@Composable
fun unitConvertor() {

    var inputValue by remember {
        mutableStateOf("")
    }

    var outputValue by remember {
        mutableStateOf("")
    }

    var inputUnit by remember {
        mutableStateOf("Select")
    }

    var outputUnit by remember {
        mutableStateOf("Select")
    }

    var iExpanded by remember {
        mutableStateOf(false)
    }

    var oExpanded by remember {
        mutableStateOf(false)
    }

    val conversionFactor = remember {
        mutableStateOf(1.00)
    }

    val oconversionFactor = remember {
        mutableStateOf(1.00)
    }

    var resultUnit by remember {
        mutableStateOf("")
    }

    fun convertUnit(){
        //elvis operator {if users enteries the value to string then it will return null but we make it to default of 0.0}
        val inputvalueDouble = inputValue.toDoubleOrNull() ?: 0.0
        val result = (inputvalueDouble * conversionFactor.value ) //conversion of input ot meters
        // now conversion of different meters to required units
        val finalResult = (result * oconversionFactor.value).roundToInt()
        outputValue = finalResult.toString()
    }


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Unit Convertor",style = MaterialTheme.typography.headlineLarge, fontFamily = FontFamily.Serif)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = inputValue, onValueChange = {
            inputValue = it
        }, label = {
            Text(text = "Enter the value")
        })
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Box() {
                Button(onClick = {
                    iExpanded = true
                }) {
                    Text(text = inputUnit)
                    Icon(
                        Icons.Default.ArrowDropDown,
                        contentDescription = "Arrow Down"
                    )
                }
                DropdownMenu(expanded = iExpanded, onDismissRequest = { iExpanded = false }) {
                    DropdownMenuItem(text = { Text(text = "Centimeters") }, onClick = {
                        iExpanded = false
                        inputUnit = "Centimeters"
                        conversionFactor.value = 0.01
                    })
                    DropdownMenuItem(text = { Text(text = "Meters") }, onClick = {
                        iExpanded = false
                        inputUnit = "Meters"
                        conversionFactor.value = 1.0
                    })
                    DropdownMenuItem(text = { Text(text = "Feet") }, onClick = {
                        iExpanded = false
                        inputUnit = "Feet"
                        conversionFactor.value = 0.3048
                    })
                    DropdownMenuItem(text = { Text(text = "Millimeters") }, onClick = {
                        iExpanded = false
                        inputUnit = "Millimeters"
                        conversionFactor.value = 0.001
                    })
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Box() {
                Button(onClick = { oExpanded = true }) {
                    Text(text = outputUnit)
                    Icon(
                        Icons.Default.ArrowDropDown,
                        contentDescription = "Arrow Down"
                    )
                }
                DropdownMenu(expanded = oExpanded, onDismissRequest = { oExpanded = false }) {
                    DropdownMenuItem(
                        text = { Text(text = "Centimeters") },
                        onClick = {
                            resultUnit ="Centimeters"
                            oExpanded = false
                            outputUnit = "Centimeter"
                            oconversionFactor.value = 100.0
                            convertUnit()
                        })
                    DropdownMenuItem(
                        text = { Text(text = "Meters") },
                        onClick = {
                            resultUnit ="Meters"
                            oExpanded = false
                            outputUnit = "Meter"
                            oconversionFactor.value = 1.0
                            convertUnit()
                        })
                    DropdownMenuItem(
                        text = { Text(text = "Feet") },
                        onClick = {
                            resultUnit ="Feet"
                            oExpanded = false
                            outputUnit = "Feet"
                            oconversionFactor.value = 3.28084
                            convertUnit()
                        })
                    DropdownMenuItem(
                        text = { Text(text = "Millimeters") },
                        onClick = {
                            resultUnit ="Millimeter"
                            oExpanded = false
                            outputUnit = "Millimeters"
                            oconversionFactor.value = 1000.0
                            convertUnit()
                        })
                }

            }

        }
        Spacer(modifier = Modifier.height(5.dp))
        Row {
            Text(text = "Result",style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = outputValue,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = resultUnit,
                style = MaterialTheme.typography.headlineMedium
            )
        }


    }
}

@Preview(showSystemUi = true)
@Composable
fun previewFunction() {
    unitConvertor()
}

