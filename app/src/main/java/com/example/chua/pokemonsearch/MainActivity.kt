package com.example.chua.pokemonsearch

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat
import ir.mirrajabi.searchdialog.core.SearchResultListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_row.*
import okhttp3.*
import org.jetbrains.anko.doAsync
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private var txtSearch: TextView? = null
    private var imgPokemon: ImageView? = null
    private var txtPokemon: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        setContentView(R.layout.activity_main)

        findView()

        relativeItem!!.visibility = View.GONE

        var pokemonName = ""
        var pokemonNumber = 0

        txtSearch!!.setOnClickListener {
            SimpleSearchDialogCompat(this@MainActivity, "Search", "Enter a Pokemon.", null,
                    pokemonList(), SearchResultListener { baseSearchDialogCompat, item, position ->
                txtSearch!!.text = item.title
                pokemonName = item.title
                pokemonNumber = item.getNumber()
                Toast.makeText(this@MainActivity, pokemonName, Toast.LENGTH_SHORT).show()
                fetch(pokemonName, pokemonNumber.toString())
                baseSearchDialogCompat.dismiss()
            }).show()
        }
    }

    private fun findView() {
        txtSearch = findViewById(R.id.txtSearch)
        imgPokemon = findViewById(R.id.imgPokemon)
        txtPokemon = findViewById(R.id.txtPokemon)
    }

    private fun pokemonList(): ArrayList<SearchModel> {
        val pokemons = ArrayList<SearchModel>()
        pokemons.add(SearchModel("Bulbasaur", 1))
        pokemons.add(SearchModel("Ivysaur", 2))
        pokemons.add(SearchModel("Venusaur", 3))
        pokemons.add(SearchModel("Charmander", 4))
        pokemons.add(SearchModel("Charmeleon", 5))
        pokemons.add(SearchModel("Charizard", 6))
        pokemons.add(SearchModel("Squirtle", 7))
        pokemons.add(SearchModel("Wartortle", 8))
        pokemons.add(SearchModel("Blastoise", 9))
        pokemons.add(SearchModel("Caterpie", 10))
        pokemons.add(SearchModel("Metapod", 11))
        pokemons.add(SearchModel("Butterfree", 12))
        pokemons.add(SearchModel("Weedle", 13))
        pokemons.add(SearchModel("Kakuna", 14))
        pokemons.add(SearchModel("Beedrill", 15))
        pokemons.add(SearchModel("Pidgey", 16))
        pokemons.add(SearchModel("Pidgeotto", 17))
        pokemons.add(SearchModel("Pidgeot", 18))
        pokemons.add(SearchModel("Rattata", 19))
        pokemons.add(SearchModel("Raticate", 20))

        return pokemons
    }

    private fun fetch(pokemonName: String, pokemonNumber: String) {

        doAsync {
            runOnUiThread {
                progressBar!!.visibility = View.VISIBLE
                relativeItem!!.visibility = View.GONE
            }
            val result = "https://pokeapi.co/api/v2/pokemon/$pokemonNumber"
            val pokemonClient = OkHttpClient()
            val pokemonRequest = Request.Builder().url(result).build()
            pokemonClient.newCall(pokemonRequest).enqueue(object : Callback {
                override fun onResponse(call: Call?, response: Response?) {
                    val Body = response?.body()?.string()
                    val Gson = GsonBuilder().create()
                    val Feed = Gson.fromJson(Body, Pokemon::class.java)

                    runOnUiThread {
                        progressBar!!.visibility = View.GONE
                        relativeItem!!.visibility = View.VISIBLE
                        txtPokemon!!.text = pokemonName // I used this to capitalize the first letter
                        /* pokemonFeed.name == pokemonName. Difference is the capitalization of first letter */
                        Picasso.with(this@MainActivity).load(Feed.sprites.front_default).into(imgPokemon)
                    }
                }
                override fun onFailure(call: Call?, e: IOException?) {
                    runOnUiThread {
                        progressBar!!.visibility = View.GONE
                    }
                    Toast.makeText(this@MainActivity, "Failed to execute", Toast.LENGTH_LONG).show()
                    Toast.makeText(this@MainActivity, "Internet is too slow.", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}