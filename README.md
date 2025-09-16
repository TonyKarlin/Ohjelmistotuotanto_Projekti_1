# Ohjelmistotuotantoprojekti

- Tony Karlin
- Onni Kivinen
- Joni Heikkilä
- Jarkko Kärki

## JaCoCo report

Java Code Coverage reports for both Backend and Frontend directories.

```powershell
cd .\Frontend; mvn clean test jacoco:report
```

```powershell
cd .\Backend; mvn clean test jacoco:report
```

The JaCoCo reports can be found in the `target/site/jacoco` directory within each respective project.
