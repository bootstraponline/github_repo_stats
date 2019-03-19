import main.data.GHList
import main.util.Json
import main.util.issuesJson
import main.util.prsJson
import java.time.Duration
import java.time.ZonedDateTime

typealias Year = Int

object CreateReport {

    data class Metrics(
        var opened: Int,
        var closed: Int,
        var times: MutableList<Long?>
    )

    private fun csvStats(prs: GHList) {
        val stats = mutableMapOf<Year, Metrics>()

        // Desired output
        // year: 2019
        // PRs opened:
        // PRs closed:
        // avg days to close:

        prs.issues.forEach {
            var time: Long? = null

            val createdAt = ZonedDateTime.parse(it.createdAt)

            if (it.closedAt != null) {
                val closedAt = ZonedDateTime.parse(it.closedAt)
                time = closedAt.toEpochSecond() - createdAt.toEpochSecond()
            }

            val year = createdAt.year
            val closed = if (it.closedAt == null) 0 else 1

            if (stats[year] == null) {
                stats[year] = Metrics(
                    opened = 1,
                    closed = closed,
                    times = mutableListOf(time)
                )
            } else {
                val key = stats[year]
                if (key != null) {
                    key.opened += 1
                    key.closed += closed
                    key.times.add(time)
                }
            }
        }

        var allIssues = 0
        val columns = listOf("Year", "Opened", "Closed", "Avg days to close")
        println(columns.joinToString { it })

        stats.forEach { year, stat ->
            allIssues += stat.opened
            val row = mutableListOf<Any>()
            row.add(year)
            row.add(stat.opened)
            row.add(stat.closed)

            val times = stat.times
                .mapNotNull { it }
            val timeCount = times.size
            val avgTime = times.sum() / timeCount

            val timeString = Duration.ofSeconds(avgTime).toDays().toString()

            row.add(timeString)

            println(row.joinToString { it.toString() })
        }

        if (prs.totalCount != allIssues) {
            throw RuntimeException("Total count mismatch: ${prs.totalCount} != $allIssues")
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Pull Requests")
        val prs = Json.readFile(prsJson, GHList::class.java)
        csvStats(prs)

        println("\nIssues")
        val issues = Json.readFile(issuesJson, GHList::class.java)
        csvStats(issues)
    }
}
