package com.beiyuan.gatewayapi.datasource.unpooled;

import com.beiyuan.gatewayapi.datasource.Connection;
import com.beiyuan.gatewayapi.datasource.DataSource;
import com.beiyuan.gatewayapi.datasource.DataSourceType;
import com.beiyuan.gatewayapi.datasource.connection.DubboConnection;
import com.beiyuan.gatewayapi.http.HttpStatement;
import com.beiyuan.gatewayapi.session.Configuration;

/**
 * @author: beiyuan
 * @date: 2023/5/16  15:07
 */
public class UnpooledDataSource implements DataSource {

    private HttpStatement httpStatement;

    private Configuration configuration;

    private DataSourceType dataSourceType;

    public void setHttpStatement(HttpStatement httpStatement) {
        this.httpStatement = httpStatement;
    }


    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public void setDataSourceType(DataSourceType dataSourceType) {
        this.dataSourceType = dataSourceType;
    }

    @Override
    public Connection getConnection() {

        switch (dataSourceType){
            case HTTP:
                //未来补充
                break;
            case DUBBO:

               return new DubboConnection(
                        configuration.getApplicationConfig(httpStatement.getApplicationName()),
                        configuration.getRegistryConfig(httpStatement.getApplicationName()),
                        configuration.getReferenceConfig(httpStatement.getInterfaceName())
                        );
            default:
                break;
        }

        throw new RuntimeException("The TataSourceType of "+dataSourceType+" is not supported");
    }
}
