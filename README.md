<h2><b>Build Status</b></h2>

[![Build Status](https://travis-ci.org/peterjot/EthereumSmartContractIdentifier.svg?branch=master)](https://travis-ci.org/peterjot/EthereumSmartContractIdentifier)

<br/>

<h2><b>Support</b></h2>
If you need support:<br/>
<a href="mailto:piotrekjasina@gmail.com">Send me an email</a><br/>
<br/><br/><br/>

<h2><b>How to compile?</b></h2>
```bash
git clone https://github.com/peterjot/EthereumSmartContractIdentifier.git
cd EthereumSmartContractIdentifier/
chmod +x mvnw 
./mvnw install -DskipTests=true -Dmaven.javadoc.skip-true -B
```
<br/>


<h2><b>How to run?</b></h2>
Java8 is required!
Mongodb is required!
```bash example
java -jar ./target/contract-identifier-1.1.0-SNAPSHOT.jar --DB_LOGIN=test --DB_PASSWORD=testte
st1 --DB_NAME=test1 --DB_PORT=29904 --DB_HOST=ds129904.mlab.com --ADMIN_LOGIN=123 --ADMIN_PASSWORD=321 --SERVER_PORT=80```
