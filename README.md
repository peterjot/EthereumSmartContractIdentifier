<h2><b>Ethereum Smart Contract Identifier</b></h2>
App that help you identify ethereum smart contract from bytecode

</br>

<h2><b>Build Status</b></h2>

[![Build Status](https://travis-ci.org/peterjot/EthereumSmartContractIdentifier.svg?branch=master)](https://travis-ci.org/peterjot/EthereumSmartContractIdentifier)

<h2><b>Support</b></h2>
If you need support:<br/>
<a href="mailto:piotrekjasina@gmail.com">Send me an email</a><br/>
<br/>

<h2><b>How to compile?</b></h2>

```bash
git clone https://github.com/peterjot/EthereumSmartContractIdentifier.git
cd EthereumSmartContractIdentifier/
chmod +x mvnw 
./mvnw clean install -DskipTest=true -Dmaven.javadoc.skip-true -B
```

<h2><b>How to run?</b></h2>

```bash
java -jar ./target/contract-identifier-1.1.0-SNAPSHOT.jar --DB_LOGIN=myDbLogin --DB_PASSWORD=myDbPassword --DB_NAME=myDbName --DB_PORT=myGreatPort --DB_HOST=myDatabaseHost --ADMIN_LOGIN=myAdminLogin --ADMIN_PASSWORD=myAdminPassword --SERVER_PORT=myServerPort
```

<h2><b>docker-compose</b></h2>

```bash
git clone https://github.com/peterjot/EthereumSmartContractIdentifier.git
cd EthereumSmartContractIdentifier
docker-compose up
```
