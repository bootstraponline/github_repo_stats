package main.util

import com.google.gson.GsonBuilder

val pageSize = 100
val gson = GsonBuilder().setPrettyPrinting().create()
val issuesJson = "issues.json"
val prsJson = "prs.json"
