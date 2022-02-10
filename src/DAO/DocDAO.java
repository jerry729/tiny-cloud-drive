package DAO;

import filesystem.document.Doc;

import java.util.List;

/**
 * @author frank
 */
public interface DocDAO {

    Doc getDocByID(int Id);


    /**
     * @author      :riri
     * @date        :2020/12/13 11:15
     * @description :TODO 按编号查找档案 未找到返回 null
     *
     * @throws      :
     * @parameter   :connection
    doc
     * @return      :Doc
     */
     Doc getDoc(Doc doc);


    /**
     * @author      :riri
     * @date        :2020/12/13 11:27
     * @description :TODO 插入新档案
     *
     * @throws      :
     * @parameter   :connection
    doc
     * @return      :void
     */
    boolean insertDoc(Doc doc);


    /**
     * @author      :riri
     * @date        :2020/12/13 11:26
     * @description :TODO 列出所有档案
     *
     * @throws      :
     * @parameter   :connection
     * @return      :java.util.ArrayList<filesystem.document.Doc>
     */
    List<Doc> listAllDoc();

    boolean deleteDoc(Doc doc);
}
