# Security and privacy notes

This is a portfolio Android application, not a production HR platform.

- Employee names, addresses, telephone numbers, email addresses, salaries and profile photos are personal information. Do not publish actual employee data or SQLite databases.
- App records are stored locally on the device using Room. The app has no cloud API or organization authentication; access is controlled largely by the device environment.
- The current Room builder uses `fallbackToDestructiveMigration()`. A future incompatible schema migration can delete user data. Create explicit database migrations before production use.
- Review Android backup and data-extraction policies before using the app with real personal information; device backups may otherwise capture app data.
- Review the permission and image lifecycle before deployment, and delete orphaned profile photos when appropriate.
- Keep signing credentials and `local.properties` out of version control.
- For vulnerabilities, contact the maintainer privately using their GitHub profile contact information rather than posting sensitive details in a public issue.
