package com.cspark.books.toby.sqlservice;

import com.cspark.books.toby.dao.UserDao;
import com.cspark.books.toby.sqlservice.jaxb.SqlType;
import com.cspark.books.toby.sqlservice.jaxb.Sqlmap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;

/**
 * Created by cspark on 2015. 12. 28..
 */
public class JaxbXmlSqlReader implements SqlReader {

    public static final String DEFAULT_SQLMAP_FILE = "sqlmap.xml";

    private String sqlmapFile = DEFAULT_SQLMAP_FILE;

    public void setSqlmapFile(String sqlmapFile) {
        this.sqlmapFile = sqlmapFile;
    }

    @Override
    public void read(SqlRegistry sqlRegistry) {
        String contextPath = Sqlmap.class.getPackage().getName();

        try {
            JAXBContext context = JAXBContext.newInstance(contextPath);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            InputStream is = UserDao.class.getResourceAsStream(sqlmapFile);
            Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(is);

            for (SqlType sql : sqlmap.getSql()) {
                sqlRegistry.registry(sql.getKey(), sql.getValue());
            }

        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }
}
