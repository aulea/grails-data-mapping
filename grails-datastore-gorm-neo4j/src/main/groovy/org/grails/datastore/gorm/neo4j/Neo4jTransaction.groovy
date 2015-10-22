/* Copyright (C) 2010 SpringSource
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.grails.datastore.gorm.neo4j

import groovy.transform.CompileStatic
import org.neo4j.graphdb.GraphDatabaseService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.grails.datastore.mapping.transactions.Transaction
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.support.DefaultTransactionDefinition

/**
 * Represents a Neo4j transaction
 *
 * @author Stefan Armbruster <stefan@armbruster-it.de>
 * @author Graeme Rocher
 */
@CompileStatic
class Neo4jTransaction implements Transaction<org.neo4j.graphdb.Transaction> {

    protected final Logger log = LoggerFactory.getLogger(getClass())

    boolean active = true

    GraphDatabaseService databaseService
    org.neo4j.graphdb.Transaction transaction
    TransactionDefinition transactionDefinition

    Neo4jTransaction(GraphDatabaseService databaseService, TransactionDefinition transactionDefinition = new DefaultTransactionDefinition()) {
        transaction = databaseService.beginTx()
        this.databaseService = databaseService;
        this.transactionDefinition = transactionDefinition
    }

    void commit() {
        transaction.success()
        active = false
    }

    void rollback() {
        transaction.failure()
        active = false
    }

    org.neo4j.graphdb.Transaction getNativeTransaction() {
        return transaction;
    }

    boolean isActive() {
        active
    }

    void setTimeout(int timeout) {
        throw new UnsupportedOperationException()
    }
}
