package com.example.aduv

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aduv.ui.theme.ADUVTheme
import com.example.aduv.ui.theme.blue
import com.example.aduv.ui.theme.grayFont
import com.example.aduv.ui.theme.pinkitemshadow
import com.example.aduv.ui.theme.yellow1
import com.example.aduv.ui.theme.yellow2
import com.example.aduv.ui.theme.blueColor
import com.google.firebase.firestore.FirebaseFirestore

class UpdateProducts : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ADUVTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold {

                        CenterAlignedTopAppBar(
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = blueColor,
                                titleContentColor = Color.White,
                                navigationIconContentColor = Color.White,
                                actionIconContentColor = Color.White
                            ),
                            title = {
                                Text(
                                    text = "Cập Nhật Sản Phẩm",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            navigationIcon = {
                                IconButton(onClick = {this@UpdateProducts.startActivity(Intent(this@UpdateProducts, ProductDetails::class.java))}) {
                                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                                }
                            },
//                            actions = {
//                                IconButton(onClick = {/* Do Something*/ }) {
//                                    Icon(imageVector = Icons.Filled.Settings, null)
//                                }
//                            }
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(0.dp, 100.dp, 0.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ){

                        updateProductUI(
                            LocalContext.current,
                            intent.getStringExtra("productName"),
                            intent.getStringExtra("productType"),
                            intent.getStringExtra("productPrice"),
                            intent.getStringExtra("productDescription"),
                            intent.getStringExtra("productImage"),
                            intent.getStringExtra("productID"),
                        )

                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun updateProductUI(
    context: Context,
    name: String?,
    type: String?,
    price: String?,
    description: String?,
    imgUrl: String?,
    productID: String?
){
    val categories = arrayOf("Phụ Kiện Tóc", "Nhẫn", "Vòng Tay", "Vòng Cổ")
    var expanded by remember { mutableStateOf(false) }

    val productName = remember {
        mutableStateOf(name)
    }
    val productType = remember {
        mutableStateOf(type)
    }
    val productPrice = remember {
        mutableStateOf(price)
    }
    val productDescription = remember {
        mutableStateOf(description)
    }
    val productImage = remember {
        mutableStateOf(imgUrl)
    }

    val newImageUrl = remember {
        mutableStateOf<String?>(null)
    }
    Column(
        modifier = Modifier
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,

    ) {

        SelectItemImageSection{
//                imageUrl -> productImage.value = imageUrl
                imageUrl -> newImageUrl.value = imageUrl

        }
        Spacer(modifier = Modifier.size(7.dp))
        OutlinedTextField(
            value = productName.value.toString(),
            onValueChange = { productName.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            shape = RoundedCornerShape(10.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.White,
                focusedBorderColor = blueColor,
                unfocusedBorderColor = blue
            ),
            label = { Text(text = "Tên Sản Phẩm", color = grayFont)}
        )
        Spacer(modifier = Modifier.size(7.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                },
            ) {
                OutlinedTextField(
                    value = productType.value.toString(),
                    onValueChange = {productType.value = it},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                        .height(59.dp),
                    shape = RoundedCornerShape(10.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.White,
                        focusedBorderColor = blueColor,
                        unfocusedBorderColor = blue
                    ),
                    label = { Text(text = "Loại Sản Phẩm", color = grayFont)}
                )

                ExposedDropdownMenu(
                    modifier = Modifier
                        .background(Color.White),
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categories.forEach { item ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = item) },
                            onClick = {
                                productType.value = item
                                expanded = false
                                Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.size(7.dp))
        OutlinedTextField(
            value = productPrice.value.toString(),
            onValueChange = { productPrice.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            shape = RoundedCornerShape(10.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.White,
                focusedBorderColor = blueColor,
                unfocusedBorderColor = blue
            ),
            label = { Text(text = "Giá Sản Phẩm", color = grayFont)}
        )
        Spacer(modifier = Modifier.size(7.dp))
        OutlinedTextField(
            value = productDescription.value.toString(),
            onValueChange = { productDescription.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            shape = RoundedCornerShape(10.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.White,
                focusedBorderColor = blueColor,
                unfocusedBorderColor = blue
            ),
            label = { Text(text = "Mô Tả", color = grayFont)}
        )
        Spacer(modifier = Modifier.size(30.dp))
        Button(
            onClick = {
                if (TextUtils.isEmpty(productName.value.toString())) {
                    Toast.makeText(context, "Hãy nhập tên sản phẩm", Toast.LENGTH_SHORT)
                        .show()
                } else if (TextUtils.isEmpty(productType.value.toString())) {
                    Toast.makeText(
                        context,
                        "Hãy chọn loại sản phẩm",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }else if (TextUtils.isEmpty(productPrice.value.toString())) {
                    Toast.makeText(
                        context,
                        "Hãy nhập giá sản phẩm",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else if (TextUtils.isEmpty(productDescription.value.toString())) {
                    Toast.makeText(
                        context,
                        "Hãy nhâp mô tả",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else {
                    val imageUrl = newImageUrl.value ?: productImage.value
                    updateDataToFirebase(
                        productID,
                        productName.value,
                        productType.value,
                        productPrice.value,
                        productDescription.value,
                        imageUrl,
                        context
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 3.dp,
                pressedElevation = 6.dp
            ),
            colors = ButtonDefaults.buttonColors(
                containerColor = blueColor
            ),
//                border = BorderStroke(0.5.dp, Color.Red)
        ) {
            Text(text = "Cập Nhật", fontWeight = FontWeight.Bold,fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.size(10.dp))
        TextButton(onClick = {
            context.startActivity(Intent(context, ProductDetails::class.java))
        },
        ) {
            Text(
                text = "Thoát",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = blueColor,
            )
        }
    }

}

private fun updateDataToFirebase(
    productID: String?,
    name: String?,
    type: String?,
    price: String?,
    description: String?,
    imgUrl: String?,
    context: Context
) {
    val updatedProduct = ProductData(productID, name, type, price, description, imgUrl)

    // getting our instance from Firebase Firestore.
    val db = FirebaseFirestore.getInstance();
    db.collection("Products").document(productID.toString()).set(updatedProduct)
        .addOnSuccessListener {
            // on below line displaying toast message and opening
            // new activity to view courses.
            Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show()
            context.startActivity(Intent(context, ProductDetails::class.java))
            //  finish()

        }.addOnFailureListener {
            Toast.makeText(context, "Lỗi cập nhật sản phẩm : " + it.message, Toast.LENGTH_SHORT)
                .show()
        }
}
