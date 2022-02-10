package DAO.implement;

import DAO.BaseDAO;
import DAO.DocDAO;
import filesystem.document.Doc;

import java.util.List;

/**
 * @author :frank
 * @date :11:30 2020/12/13
 * @description :TODO
 */
public class DocDAOImpl extends BaseDAO<Doc> implements DocDAO {
    @Override
    public Doc getDocByID(int Id) {
        Doc doc = null;
        String sql =
                "SELECT Id,creator,timestamp,description,filename "+
                "FROM doc_info " +
                "WHERE Id = ?";
        doc = getInstance(Doc.class,sql,Id);
        return  doc;
    }

    @Override
     public Doc getDoc(Doc doc) {

        Doc doc1 = null;
        String sql =
                "SELECT Id,creator,timestamp,description,filename " +
                "FROM doc_info " +
                "WHERE Id = ?";
        doc1 = getInstance(Doc.class,sql,doc.getId());
        return doc1;
    }


    @Override
    public boolean insertDoc(Doc doc) {

        String sql =
                "INSERT INTO doc_info(Id,creator,timestamp,description,filename)" +
                "VALUES(?,?,?,?,?)";
        update(sql,doc.getId(),doc.getCreator(),doc.getTimestamp(),
                doc.getDescription(),doc.getFilename());
        return true;
    }

    @Override
    public List<Doc> listAllDoc() {

        String sql = "SELECT * FROM doc_info";
        List<Doc> list = getInstanceList(Doc.class,sql);
        return list;
    }

    @Override
    public boolean deleteDoc(Doc doc){
        String sql =
                "DELETE FROM doc_info " +
                "WHERE Id = ?";
        update(sql,doc.getId());
        return true;
    }


}
