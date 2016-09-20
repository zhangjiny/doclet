package tag;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhaoshiqiang on 2016/9/10.
 */
public class MethodType implements Serializable{
    /*
    *����������л���浽Ӳ���ϣ����Ǻ�����ȴ���������field(���ӻ���ٻ����)��
    * ���㷴���л�ʱ���ͻ����Exception�ģ������ͻ���ɲ������Ե�����
    *����serialVersionUID��ͬʱ�����ͻὫ��һ����field��type��ȱʡֵ��ֵ(��int�͵���0,String�͵���null��)��
    * ������Աܿ��������Ե����⡣������ø�serialVersionUID��ֵ
    * */
    private static final long serialVersionUID = 7991552226614088458L;
    private List<Tag> tagList;
    private List<Tag> classList;
    private static Map<String,String> template;
    private Map<String,String> outpath;

    public List<Tag> getTagList() {
        return tagList;
    }

    public void setTagList(List<Tag> tagList) {
        this.tagList = tagList;
    }

    public List<Tag> getClassList() {
        return classList;
    }

    public void setClassList(List<Tag> classList) {
        this.classList = classList;
    }

    public static Map<String, String> getTemplate() {
        return template;
    }

    public static void setTemplate(Map<String, String> template) {
        MethodType.template = template;
    }

    public Map<String, String> getOutpath() {
        return outpath;
    }

    public void setOutpath(Map<String, String> outpath) {
        this.outpath = outpath;
    }


    public Map<String,List> transformTagvalue(){
        Map<String,List> map = new HashMap<String,List>();
        for (Tag tag : tagList){
            map.put(tag.getName(),tag.transformTagvalue());
//            System.out.println(tag.getName()+"---->"+tag.transformTagvalue());
        }

        for (Tag tag : classList){
            map.put("class_" + tag.getName(), tag.transformTagvalue());
//            System.out.println(tag.getName() + "---->" + tag.transformTagvalue());
        }

        return map;
    }
}
