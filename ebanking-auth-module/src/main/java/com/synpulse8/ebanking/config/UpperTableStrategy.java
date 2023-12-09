package com.synpulse8.ebanking.config;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

public class UpperTableStrategy extends PhysicalNamingStrategyStandardImpl {

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
        var tableName = name.getText().toUpperCase();
        return Identifier.toIdentifier(tableName);
    }

}