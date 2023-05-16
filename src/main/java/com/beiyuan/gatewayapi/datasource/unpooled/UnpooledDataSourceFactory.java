package com.beiyuan.gatewayapi.datasource.unpooled;

import com.beiyuan.gatewayapi.datasource.DataSource;
import com.beiyuan.gatewayapi.datasource.DataSourceFactory;
import com.beiyuan.gatewayapi.datasource.DataSourceType;
import com.beiyuan.gatewayapi.session.Configuration;

/**
 * @author: beiyuan
 * @date: 2023/5/16  15:08
 */
public class UnpooledDataSourceFactory implements DataSourceFactory {


    private UnpooledDataSource unpooledDataSource;

    public UnpooledDataSourceFactory() {
        this.unpooledDataSource =new UnpooledDataSource();
    }

    //用set方法是为了可更换。只用set方法就得每次都创建。都用就不好说
    @Override
    public void SetProperties(Configuration configuration, String uri) {
        unpooledDataSource.setConfiguration(configuration);
        unpooledDataSource.setDataSourceType(DataSourceType.DUBBO);
        unpooledDataSource.setHttpStatement(configuration.getHttpStatement(uri));
    }

    @Override
    public DataSource getDataSource() {

        return unpooledDataSource;
    }
}
