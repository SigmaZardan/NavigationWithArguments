package com.example.navigationwitharguments

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.navigationwitharguments.ui.theme.NavigationWithArgumentsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NavigationWithArgumentsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    GreetingApp()
                }
            }
        }
    }
}

enum class Routes {
    HOME, DETAIL
}

@Composable
fun GreetingApp() {
    val navController: NavHostController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.HOME.name) {
        composable(route = Routes.HOME.name) {
            HomeScreen(persons = persons, onPersonItemClick = {
                navController.navigate("${Routes.DETAIL.name}/${it}")
            })
        }
        composable(
            route = "${Routes.DETAIL.name}/{personItem}",
            arguments = listOf(navArgument("personItem") {
                type = NavType.IntType
            })
        ) {
            val currentPersonId = it.arguments?.getInt("personItem")
            val person = persons.find { person -> person.id == currentPersonId }


            DetailScreen(person ?: Person("bibek", address = "pokhara", age = 32, id = 90))

        }
    }
}


data class Person(
    val name: String, val address: String, val age: Int, val id: Int
)

// i do not need state here
// so i wont be using viewmodel
// but the data to pass is here
val persons = listOf(
    Person("John Doe", "123 Main St", 25, 1),
    Person("Jane Smith", "456 Oak St", 30, 2),
    Person("Bob Johnson", "789 Elm St", 22, 3)
    // Add more persons as needed
)


@Composable
fun HomeScreen(
    persons: List<Person>,
    modifier: Modifier = Modifier,
    onPersonItemClick: (Int) -> Unit
) {
    LazyColumn(modifier = modifier) {
        items(persons, key = { it.id }) { person ->
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .clickable {
                        onPersonItemClick(person.id)
                    }
            ) {
                Column {
                    Text(person.name, style = MaterialTheme.typography.displayMedium)
                    Text(person.address, style = MaterialTheme.typography.displayMedium)
                    Text("${person.age}", style = MaterialTheme.typography.displayMedium)
                }

            }

        }

    }
}


@Composable
fun DetailScreen(person: Person) {
    Column(
        verticalArrangement = Arrangement.Center
    ) {
        Text(person.name)
        Text(person.address)
        Text("${person.age}")
    }
}