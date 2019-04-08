package org.happybean.butler.remote;

import java.sql.Connection;

/**
 * @author wgt
 * @date 2019-04-08
 * @description
 **/
public class BuTransactional extends RemoteTx{

    private Connection connection;

    private Task task;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
