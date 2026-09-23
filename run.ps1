while (-Not (Test-Path "apache-maven-3.9.6\bin\mvn.cmd")) {
    Start-Sleep -Seconds 5
}
.\apache-maven-3.9.6\bin\mvn.cmd clean package cargo:run
