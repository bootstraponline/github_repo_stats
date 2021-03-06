# GitHub Repo Stats.

Calculate issue and PR stats for a GitHub repo.

- Run `DownloadData.kt` to create `prs.json` and `issues.json`
- Run `CreateReport.kt` to output a report based on the JSON files

# Example (Google Sheets)

<img src="./example_stats.png" width="30%" />

# GraphQL links

- https://developer.github.com/v4/explorer/
- https://github.com/apollographql/intellij-graphql
- https://github.com/apollographql/apollo-android
- https://insomnia.rest/
  - Use Bearer token auth to authenticate.
  - Restart app if schema isn't fetched
- https://github.com/jimkyndemeyer/js-graphql-intellij-plugin
  - Doesn't work with Community Edition
- https://github.com/apollographql/apollo-tooling#apollo-schemadownload-output
- `apollo schema:download --endpoint=https://api.github.com/graphql --header="Authorization: Bearer $GITHUB_TOKEN"`
- https://www.apollographql.com/docs/ios/downloading-schema.html
- Regenerate the GraphQL classes `gradle generateMainApolloClasses`
