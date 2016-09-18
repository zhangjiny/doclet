import com.sun.javadoc.*;
import com.sun.tools.doclets.formats.html.ConfigurationImpl;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.*;
import java.util.*;

/**
 * @mytag class mytag
 */
public class doclet2 {
    //����ص�ע��
    private static String CLASS_DESCRIBE = "description";   //������������������û�����Ϣ
    private static String CLASS_URI = "uri";   //��ķ���·��
    //������ص�ע��
    private static String METHOD_URI = "uri";   //��������·��
    private static String METHOD_TYPE = "type"; //�������
    private static String METHOD_DESCRIBE = "description"; //��������
    private static String METHOD_PATH = "path"; //UE������ҵ��ӿ�
    private static String METHOD_SUPPORT = "datatype";   //֧�ָ�ʽ��json��
    private static String METHOD_REQUESTTYPE = "method";   //����ʽ��get/post��
    private static String METHOD_PARAM = "param";   //�������������������ʽΪ������~��ѡ~����~˵������Ϊ�Ǳ�ѡ����˵��ʲô����²�ѡ��
    private static String METHOD_NOTICE = "notice"; //ע������
    private static String METHOD_RETURNJSON = "returnjson"; //���ص�json��ʽ
    private static String METHOD_RETURNPARAM = "returnparam";   //�������������������ʽΪ������~����~�ֶ�˵��

    private static String DIRPATH="D:/api2/";

    private static String BASEPATH = "com.bluemobi.bluecollar.network";

    public static boolean start(RootDoc root) {

        try {
            doc(root.classes());
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {

            //����configuration����
            Configuration cfg = new Configuration();
            //ָ��ģ��·��
            File file = new File("");
            //Ϊʲô�����·������D:\ProgramZhaoShiqiang\JavaWeb\DocletTest\src����D:\ProgramZhaoShiqiang\JavaWeb\DocletTest
            System.out.println(file.getAbsolutePath());
            //����Ҫ������ģ�����ڵ�Ŀ¼��������ģ���ļ�
            cfg.setDirectoryForTemplateLoading(file);
            //���ñ��뷽ʽ
            cfg.setDefaultEncoding("UTF-8");
            Template indexTemplate = cfg.getTemplate("index.ftl");

            Map indexRoot = new HashMap();
            List<Map> divlist = new ArrayList<>();
            indexRoot.put("divlist", divlist);
            indexRoot.put("date",new Date());
            File dirfile = new File("D:/api2/2.0/");
            File[] templist = dirfile.listFiles();
            for (int i = 0; i< templist.length ; i++)
            {
                if (templist[i].isFile())   //���ļ�����ȡ�ļ�����
                {
                    File readfile = new File("D:/api2/2.0/" + templist[i].getName());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(readfile),"UTF-8"));
                    StringBuffer filecontent = new StringBuffer();
                    String s = null;
                    while ((s = reader.readLine()) != null)
                    {
                        filecontent.append(s);
                    }
                    Map div = new HashMap();
                    div.put("content",filecontent.toString());
                    divlist.add(div);
                    reader.close();
                }
            }

            File indexfile = new File("D:/api2/" + "2.0.html");
            BufferedWriter write = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(indexfile),"UTF-8"));
            indexTemplate.process(indexRoot,write);
            write.flush();
            write.close();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return true;
    }

    private static String doc_class(ClassDoc classDoc, String TAG)
    {
        Tag[] Class_Describe = classDoc.tags(TAG);
        if (Class_Describe.length != 0)
        {
            return Class_Describe[0].text();
        }else {
            return null;
        }

    }

    private static String docMethod(MethodDoc methodDoc, String TAG)
    {
        Tag[] mcts = methodDoc.tags(TAG);
        if (mcts.length != 0)
            return mcts[0].text();
        else
            return null;
    }

    private static ArrayList<String> docMethodList(MethodDoc methodDoc, String TAG)
    {
        ArrayList<String> params = new ArrayList<String>();
        Tag[] mcts = methodDoc.tags(TAG);
        for (int i=0; i < mcts.length ; i++)
        {
            params.add(mcts[i].text());
        }
        return params;
    }
    /**
     * @mytag doc mytag
     * @param classDocs
     */
    private static void doc(ClassDoc[] classDocs) throws Exception {
        BufferedWriter write=null;
        for (int i = 0; i < classDocs.length; i++) {
            System.out.println(classDocs[i].commentText());
            String class_describe=doc_class(classDocs[i], CLASS_DESCRIBE);
            String class_uri=doc_class(classDocs[i], CLASS_URI);

            StringBuffer destdirname= new StringBuffer();
            destdirname.append(DIRPATH).append(doc_class(classDocs[i], CLASS_URI));
            System.out.println("destdirname is : "+destdirname);

            MethodDoc[] methods = classDocs[i].methods();

            //����configuration����
            Configuration cfg = new Configuration();
            //ָ��ģ��·��
            File file = new File("");

            //����Ҫ������ģ�����ڵ�Ŀ¼��������ģ���ļ�
            cfg.setDirectoryForTemplateLoading(file);
            //���ñ��뷽ʽ
            cfg.setDefaultEncoding("UTF-8");
            Template indexdivTemplate = cfg.getTemplate("indexdiv.ftl");

            Map indexRoot = new HashMap();
            indexRoot.put("description",class_describe);
            List<Map> readlist = new ArrayList<>();
            List<Map> writelist = new ArrayList<>();
            indexRoot.put("readlist",readlist);
            indexRoot.put("writelist",writelist);

            for(int j = 0; j< methods.length; j++){

                String method_uri=docMethod(methods[j], METHOD_URI);
                String method_path=docMethod(methods[j],METHOD_PATH);
                String method_type=docMethod(methods[j], METHOD_TYPE);

                //��ȡuri�󼴿ɵú������ƣ�API·�������캯����
                String uri = class_uri+"/"+method_uri;
                System.out.println("uri is : "+uri);

                //��ȡģ��
                Template javaTemplate = cfg.getTemplate("javaTemplate.ftl");
                Template methodTemplate = cfg.getTemplate("Method.ftl");

                //��װ���������ݶ���
                Map MethodRoot = new HashMap();
                Map JavaRoot = new HashMap();

                //��ҳ���ݵ�д��
                if (method_type.equals("write")) {

                    Map writediv = new HashMap();
                    //Ϊ�˷�ֹ��������棬������<a>��ǩ�������м��뵽һ��ʱ�������
                    writediv.put("auri",uri+".html?"+(new Date()).getTime());
                    writediv.put("uri",uri);
                    writediv.put("methodpath",method_path);
                    writelist.add(writediv);

                }
                else if (method_type.equals("read"))
                {
                    Map readdiv = new HashMap();
                    readdiv.put("uri",uri);
                    readdiv.put("auri",uri+".html?"+(new Date()).getTime());
                    readdiv.put("methodpath", method_path);
                    readlist.add(readdiv);

                }

                //���������uri������history/personal����personal��Ϊ��������
                String javaname = method_uri.substring(method_uri.lastIndexOf('/')+1,method_uri.length());
                JavaRoot.put("methodname",javaname);
                JavaRoot.put("basepath", BASEPATH);
                JavaRoot.put("uri",uri);

                MethodRoot.put("uri", uri);
                MethodRoot.put("describe", docMethod(methods[j], METHOD_DESCRIBE));
                MethodRoot.put("support", docMethod(methods[j], METHOD_SUPPORT));
                MethodRoot.put("requesttype", docMethod(methods[j], METHOD_REQUESTTYPE));
                MethodRoot.put("returnjson", docMethod(methods[j], METHOD_RETURNJSON));
                MethodRoot.put("notice", docMethod(methods[j], METHOD_NOTICE));

                List<Map> paramlist = new ArrayList<Map>();
                List<Map> javaparamlist = new ArrayList<Map>();
                MethodRoot.put("param", paramlist);
                JavaRoot.put("paramlist",javaparamlist);
                 ArrayList<String> params = docMethodList(methods[j], METHOD_PARAM);//list��ÿһ���Ӧһ���������������
                for (int k=0; k<params.size();k++)
                {
                    String data = params.get(k);   //��ȡÿһ����������
                    String[] options = data.split("~");    //�����Ҫ������

                    Map javaparam = new HashMap();
                    javaparam.put("name",options[0]);

                    Map param = new HashMap();
                    param.put("name",options[0]);
                    param.put("select",options[1]);
                    param.put("type",options[2]);
                    param.put("explain",options[3]);

                    paramlist.add(param);
                    javaparamlist.add(javaparam);
                }

                List<Map> returnparamlist = new ArrayList<Map>();
                MethodRoot.put("returnparam", returnparamlist);
                ArrayList<String> returnparams = docMethodList(methods[j], METHOD_RETURNPARAM);//list��ÿһ���Ӧһ�����ز���������
                for (int k=0; k<returnparams.size();k++)
                {
                    String data = returnparams.get(k);   //��ȡÿһ����������
                    String[] options = data.split("~");    //�����Ҫ������
                    Map returnparam = new HashMap();
                    returnparam.put("name",options[0]);
                    returnparam.put("type",options[1]);
                    returnparam.put("explain",options[2]);

                    returnparamlist.add(returnparam);
                }

                //ÿ��������Ӧ��html�ļ�
                StringBuffer filenamepath = new StringBuffer();
                filenamepath.append(destdirname.toString());
                filenamepath.append("/");
                filenamepath.append(method_uri);
                filenamepath.append(".html");
                System.out.println(filenamepath.toString());

                //ÿ��������Ӧ��java�ļ�
                StringBuffer javapath = new StringBuffer();
                javapath.append(destdirname.toString());
                javapath.append("/");
                javapath.append(method_uri);
                javapath.append("Request.java");

                //���html�ļ�
                File writefile = new File(filenamepath.toString());
                String dirMethodPath = writefile.getParent();
                File dirHtmlFile = new File(dirMethodPath);
                if (!dirHtmlFile.exists()){
                    dirHtmlFile.mkdirs();
                }
                write = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(writefile),"UTF-8"));
                methodTemplate.process(MethodRoot, write);
                write.flush();

                //���java�ļ�
                File javafile = new File(javapath.toString());
                String dirJavaPath = writefile.getParent();
                File dirJavaFile = new File(dirJavaPath);
                if (!dirJavaFile.exists()){
                    dirJavaFile.mkdirs();
                }
                write = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(javafile),"UTF-8"));
                javaTemplate.process(JavaRoot,write);
                write.flush();
            }

            StringBuffer indexnamepath = new StringBuffer();
            indexnamepath.append(DIRPATH);
            indexnamepath.append(class_uri);
            indexnamepath.append(".html");

            File indexfile = new File(indexnamepath.toString());
            String dirIndexPath = indexfile.getParent();
            File dirIndexFile = new File(dirIndexPath);
            if (!dirIndexFile.exists())
                dirIndexFile.mkdirs();
            write = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(indexfile),"UTF-8"));
            indexdivTemplate.process(indexRoot, write);
            write.flush();
        }
        if (write != null)
            write.close();

    }

    public static int optionLength(String option) {
        // Construct temporary configuration for check
        return (ConfigurationImpl.getInstance()).optionLength(option);
    }
    public static boolean validOptions(String options[][], DocErrorReporter reporter) {
        // Construct temporary configuration for check
        return (ConfigurationImpl.getInstance()).validOptions(options, reporter);
    }
}
