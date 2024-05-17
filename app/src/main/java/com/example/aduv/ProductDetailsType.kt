package com.example.aduv

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.aduv.ui.theme.ADUVTheme
import com.example.aduv.ui.theme.Pink40
import com.example.aduv.ui.theme.blueColor
import com.example.aduv.ui.theme.darkblue
import com.example.aduv.ui.theme.red
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class ProductDetailsType : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val selectedType = intent.getStringExtra("productType") ?: ""

        setContent {
            ADUVTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
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
                                    text = "Sản phẩm loại: $selectedType",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            navigationIcon = {
                                IconButton(onClick = {
                                    this@ProductDetailsType.startActivity(
                                        Intent(
                                            this@ProductDetailsType,
                                            ProductDetails::class.java
                                        )
                                    )
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = ""
                                    )
                                }
                            },
                            actions = {
                                IconButton(onClick = { }) {
                                    Icon(imageVector = Icons.Filled.List, null)
                                }
                            }
                        )
                    }

                    val productList = mutableStateListOf<ProductData>()
                    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
                    val dbProducts: CollectionReference = db.collection("Products")

                    dbProducts.whereEqualTo("productType", selectedType).get()
                        .addOnSuccessListener { queryDocumentSnapshot ->
                            if (!queryDocumentSnapshot.isEmpty) {
                                val list = queryDocumentSnapshot.documents
                                for (d in list) {
                                    val c: ProductData? = d.toObject(ProductData::class.java)
                                    c?.productID = d.id
                                    Log.e("TAG", "Product id is : " + c!!.productID)
                                    productList.add(c)
                                }
                            } else {
                                Toast.makeText(
                                    this@ProductDetailsType,
                                    "Không có dữ liệu trong Database",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }.addOnFailureListener {
                            Toast.makeText(
                                this@ProductDetailsType,
                                "Lỗi khi lấy dữ liệu.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    productDetailsTypeUI(LocalContext.current, productList)
                }
            }
        }
    }

    private fun deleteDataFromFirebase(productID: String?, context: Context) {
        val db = FirebaseFirestore.getInstance()
        db.collection("Products").document(productID.toString()).delete().addOnSuccessListener {
            Toast.makeText(context, "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show()
            context.startActivity(Intent(context, ProductDetails::class.java))
        }.addOnFailureListener {
            Toast.makeText(context, "Lỗi khi xóa sản phẩm", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("SuspiciousIndentation")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun productDetailsTypeUI(context: Context, productList: SnapshotStateList<ProductData>) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp, 70.dp, 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val selectedType = intent.getStringExtra("productType") ?: ""
            LazyColumn {
                itemsIndexed(productList) { index, item ->
                    if (item.productType == selectedType) {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = blueColor,
                            modifier = Modifier
                                .height(210.dp)
                                .padding(8.dp),
                            shadowElevation = 10.dp,
                            onClick = {
                                val i = Intent(context, UpdateProducts::class.java).apply {
                                    putExtra("productName", item.productName)
                                    putExtra("productType", item.productType)
                                    putExtra("productPrice", item.productPrice)
                                    putExtra("productDescription", item.productDescription)
                                    putExtra("productImage", item.productImage)
                                    putExtra("productID", item.productID)
                                }
                                context.startActivity(i)
                            }
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    modifier = Modifier.size(width = 160.dp, height = 160.dp)
                                ) {
                                    item.productImage?.let {
                                        Image(
                                            painter = rememberAsyncImagePainter(it),
                                            contentScale = ContentScale.None,
                                            contentDescription = null
                                        )
                                    }
                                }
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .weight(2f)
                                        .padding(horizontal = 15.dp, vertical = 0.dp),
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(24.dp),
                                        modifier = Modifier.wrapContentSize(),
                                        color = Pink40
                                    ) {
                                        item.productType?.let {
                                            Text(
                                                text = it,
                                                fontSize = 10.sp,
                                                style = MaterialTheme.typography.titleSmall,
                                                modifier = Modifier.padding(
                                                    vertical = 4.dp,
                                                    horizontal = 8.dp
                                                ),
                                                color = Color.DarkGray
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))

                                    item.productName?.let {
                                        Text(
                                            text = it,
                                            fontSize = 22.sp,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))

                                    item.productPrice?.let {
                                        Text(text = "$it vnđ")
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "5.0",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            style = MaterialTheme.typography.titleLarge
                                        )
                                        Spacer(modifier = Modifier.width(5.dp))
                                        repeat(5) {
                                            Icon(
                                                imageVector = Icons.Default.Star,
                                                tint = Color(0xFFFFBF00),
                                                contentDescription = null
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))

                                    OutlinedButton(
                                        shape = RoundedCornerShape(7.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            contentColor = Color.Black,
                                            containerColor = darkblue
                                        ),
                                        border = BorderStroke(0.5.dp, blueColor),
                                        onClick = {
                                            val i =
                                                Intent(context, UpdateProducts::class.java).apply {
                                                    putExtra("productName", item.productName)
                                                    putExtra("productType", item.productType)
                                                    putExtra("productPrice", item.productPrice)
                                                    putExtra(
                                                        "productDescription",
                                                        item.productDescription
                                                    )
                                                    putExtra("productImage", item.productImage)
                                                    putExtra("productID", item.productID)
                                                }
                                            context.startActivity(i)
                                        }
                                    ) {
                                        Text(
                                            text = "Sửa",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            style = MaterialTheme.typography.titleLarge
                                        )
                                    }
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    onClick = {
                                        deleteDataFromFirebase(item.productID, context)
                                    },
                                    color = blueColor
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "",
                                        tint = red,
                                        modifier = Modifier
                                            .size(30.dp)
                                            .padding(2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
