import Util.HandleView;
import Util.HandleXml;
import Util.TypeFactory;
import com.sun.javadoc.*;
import com.sun.javadoc.Tag;
import com.sun.tools.doclets.formats.html.ConfigurationImpl;
import tag.*;

import java.util.*;

/**
 * Created by zhaoshiqiang on 2016/9/10.
 */
public class doclet3 {

    private static String xmlpath;
    public static boolean start(RootDoc root) {
        try {
            HandleXml.handlexml(xmlpath);
            HandleView.init();
        } catch (Exception e) {
            e.printStackTrace();
        }

        PackageType packageType = TypeFactory.getPackageType();
        try {
            doc(root.classes(), packageType);
            //Ϊ�����ɶ�Ӧ���ĵ�
            try {

                HandleView.createfile(packageType.transformTagvalue(),PackageType.getOutpath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return true;
    }

    private static List<String> docClassList(ClassDoc classDoc, String TAG)
    {
        Tag[] Class_Describe = classDoc.tags(TAG);
        List<String> params = new ArrayList<String>();
        for (int i=0 ; i < Class_Describe.length ; i++){
            params.add(Class_Describe[i].text());
        }
        return params;

    }

    private static List<String> docMethodList(MethodDoc methodDoc, String TAG)
    {
        List<String> params = new ArrayList<String>();
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
    private static void doc(ClassDoc[] classDocs,PackageType packageType) throws Exception {

        List<ClassType> classTypeList = new ArrayList<ClassType>();
        packageType.setClassTypes(classTypeList);
        //���������
        for (int i = 0; i < classDocs.length; i++) {

            ClassType classType = TypeFactory.getClassType();
            classTypeList.add(classType);

            //������ע��
            ClassDoc classDoc = classDocs[i];
            List<tag.Tag> classtags = classType.getTagList();
            for (tag.Tag classtag : classtags){
                classtag.setItemvalues(docClassList(classDoc,classtag.getName()));
            }
            for (Map.Entry<String,String> outpath : classType.getOutpath().entrySet()){
                outpath.setValue(docClassList(classDoc,outpath.getKey()).get(0));
            }
            //�������и���������ע��
            List<MethodType> methodTypeList = new ArrayList<MethodType>();
            classType.setMethodTypeList(methodTypeList);
            MethodDoc[] methodDocs = classDoc.methods();
            for (int j = 0 ; j < methodDocs.length ; j ++){
                MethodType methodType = TypeFactory.getMethodType();
                methodTypeList.add(methodType);

                //������ע��
                MethodDoc methodDoc = methodDocs[j];
                List<tag.Tag> methodtags = methodType.getTagList();
                for (tag.Tag methodtag : methodtags){
                    methodtag.setItemvalues(docMethodList(methodDoc,methodtag.getName()));
                }
                //����ÿ�����������·��
                for (Map.Entry<String,String> outpath : methodType.getOutpath().entrySet()){
//                    System.out.println(outpath.getKey() + "---->" + docMethodList(methodDoc,outpath.getKey()).get(0));
                    outpath.setValue(docMethodList(methodDoc,outpath.getKey()).get(0));
                }
                //Ϊÿ���������ɶ�Ӧ���ļ�
                HandleView.createfile(methodType.transformTagvalue(),methodType.getOutpath());
            }
            //����ÿ��������·��
            for (Map.Entry<String,String> outpath : classType.getOutpath().entrySet()){
                System.out.println(outpath.getKey() + "---->" + docClassList(classDoc, outpath.getKey()).get(0));
                outpath.setValue(docClassList(classDoc, outpath.getKey()).get(0));
            }
            //Ϊÿ�������ɶ�Ӧ���ļ�
            HandleView.createfile(classType.transformTagvalue(),classType.getOutpath());
        }

    }

    public static int optionLength(String option) {
        // Construct temporary configuration for check
        if (option.equals("-xmlpath")){
            return 2;
        }else {
            return (ConfigurationImpl.getInstance()).optionLength(option);
        }
    }
    public static boolean validOptions(String options[][], DocErrorReporter reporter) {
        // Construct temporary configuration for check
        boolean foundTagOption = false;
        for (int i=0; i < options.length ; i++){
            String[] opt = options[i];
            if (opt[0].equals("-xmlpath"))
            {
                if (foundTagOption){
                    reporter.printError("Only one -tag option allowed.");
                    return false;
                }else {
                    xmlpath = opt[1];
                    foundTagOption = true;
                    return true;
                }
            }
        }
        return (ConfigurationImpl.getInstance()).validOptions(options, reporter);
    }

}
