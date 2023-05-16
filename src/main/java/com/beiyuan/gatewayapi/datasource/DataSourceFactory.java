package com.beiyuan.gatewayapi.datasource;

import com.beiyuan.gatewayapi.session.Configuration;

/**
 * @author: beiyuan
 * @date: 2023/5/16  15:08
 */
public interface DataSourceFactory {

    public void SetProperties(Configuration configuration,String uri);

    public DataSource getDataSource();
}
