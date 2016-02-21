package com.dc.esb.servicegov.export.impl;

import com.dc.esb.servicegov.entity.*;
import com.dc.esb.servicegov.service.impl.IdaServiceImpl;
import com.dc.esb.servicegov.service.impl.SDAServiceImpl;
import com.dc.esb.servicegov.service.impl.ServiceInvokeServiceImpl;
import com.dc.esb.servicegov.service.support.Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/2/17.
 */
@Component("cCFES8583ConfigExportGender")
public class CCFES8583ConfigExportGender extends ConfigExportGenerator {
    private Log log = LogFactory.getLog(CCFES8583ConfigExportGender.class);
    @Autowired
    IdaServiceImpl idaService;
    @Autowired
    SDAServiceImpl sdaService;

    @Autowired
    ServiceInvokeServiceImpl serviceInvokeService;

    private int flag;

    /**
     * 生成系统请求文件
     *
     * @param serviceInvoke
     * @param path
     */
    @Override
    public void genrateServiceSystemFile(ServiceInvoke serviceInvoke, String path) {
        try {
            Interface inter = serviceInvoke.getInter();
            if (null != inter) {
                String serviceId = serviceInvoke.getServiceId();
                String operationId = serviceInvoke.getOperationId();
                com.dc.esb.servicegov.entity.System system = serviceInvoke.getSystem();
                String fileName = null;
                if (path.indexOf("in_config") > 0) {
                    fileName = path + File.separator + "channel_" + system.getSystemAb().toLowerCase().substring(2) + "_channel_service_" + serviceId + operationId + ".xml";
                } else {
                    fileName = path + File.separator + "service_" + serviceId + operationId + "_system_" + system.getSystemAb().toLowerCase().substring(2) + "_system.xml";
                }
                flag=1;

                File file = new File(fileName);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }

                Document doc = DocumentHelper.createDocument();
                Element serviceElement = doc.addElement("sdoroot");//根节点
                addAttribute(serviceElement, "package_type", "iso8583");
                addAttribute(serviceElement, "store-mode", "GBK");
                addAttribute(serviceElement, "lenStartIndex", "2");

                Document iso8583Doc=referXmlIso8583();
                Element root=iso8583Doc.getRootElement();
                serviceElement.addElement("H1").appendAttributes(root.element("H1"));
                serviceElement.addElement("H2").appendAttributes(root.element("H2"));
                serviceElement.addElement("H3").appendAttributes(root.element("H3"));
                serviceElement.addElement("H4").appendAttributes(root.element("H4"));
                serviceElement.addElement("H5").appendAttributes(root.element("H5"));
                serviceElement.addElement("H6").appendAttributes(root.element("H6"));
                serviceElement.addElement("H7").appendAttributes(root.element("H7"));
                serviceElement.addElement("H8").appendAttributes(root.element("H8"));
                serviceElement.addElement("H9").appendAttributes(root.element("H9"));
                serviceElement.addElement("H10").appendAttributes(root.element("H10"));

                Ida reqsponseIda = idaService.getByInterfaceIdStructName(inter.getInterfaceId(), Constants.ElementAttributes.REQUEST_NAME);
                List<Ida> children = idaService.getNotEmptyByParentId(reqsponseIda.getId());

                fillContent(serviceElement, children, serviceId, operationId);

//                //两个xml文件并集,保存到已存在的xml文件中
//                if (file.exists()) {
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileName)),
//                            "utf-8"));
//                    String temp = null;
//                    StringBuffer xml = new StringBuffer();
//                    try {
//                        while ((temp = reader.readLine()) != null) {
//                            xml.append(temp);
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    Document docAll = DocumentHelper.parseText(xml.toString());
//                    mergeXmlNode(docAll.getRootElement(), serviceElement);
//                    doc = docAll;
//                }

                try {
                    OutputFormat format = OutputFormat.createPrettyPrint();
                    format.setEncoding("utf-8");
                    FileOutputStream fos = new FileOutputStream(fileName);
                    XMLWriter writer = new XMLWriter(fos, format);
                    writer.write(doc);
                    writer.close();
                } catch (IOException e) {
                    log.error(e, e);
                }
            }

        } catch (Exception e) {
            log.error(e, e);
        }
    }

    /**
     * 生成esb响应文件
     *
     * @param serviceInvoke
     * @param path
     */
    @Override
    public void genrateSystemServiceFile(ServiceInvoke serviceInvoke, String path) {
        try {
            Interface inter = serviceInvoke.getInter();
            if (null != inter) {
                String serviceId = serviceInvoke.getServiceId();
                String operationId = serviceInvoke.getOperationId();
                com.dc.esb.servicegov.entity.System system = serviceInvoke.getSystem();
                String fileName = null;
                if (path.indexOf("in_config") > 0) {
                    fileName = path + File.separator + "service_" + serviceId + operationId + "_system_" + system.getSystemAb().toLowerCase().substring(2) + "_channel.xml";
                } else {
                    fileName = path + File.separator + "channel_" + system.getSystemAb().toLowerCase().substring(2) + "_system_service_" + serviceId + operationId + ".xml";
                }
                flag=2;

                File file = new File(fileName);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }


                Document doc = DocumentHelper.createDocument();
                Element serviceElement = doc.addElement("sdoroot");//根节点
                addAttribute(serviceElement, "package_type", "iso8583");

                Document iso8583Doc=referXmlIso8583();
                Element root=iso8583Doc.getRootElement();
                serviceElement.addElement("H1").appendAttributes(root.element("H1"));
                serviceElement.addElement("H2").appendAttributes(root.element("H2"));
                serviceElement.addElement("H3").appendAttributes(root.element("H3"));
                serviceElement.addElement("H4").appendAttributes(root.element("H4"));
                serviceElement.addElement("H5").appendAttributes(root.element("H5"));
                serviceElement.addElement("H6").appendAttributes(root.element("H6"));
                serviceElement.addElement("H7").appendAttributes(root.element("H7"));
                serviceElement.addElement("H8").appendAttributes(root.element("H8"));
                serviceElement.addElement("H9").appendAttributes(root.element("H9"));
                serviceElement.addElement("H10").appendAttributes(root.element("H10"));

                Ida reqestIda = idaService.getByInterfaceIdStructName(inter.getInterfaceId(), Constants.ElementAttributes.RESPONSE_NAME);
                List<Ida> children = idaService.getNotEmptyByParentId(reqestIda.getId());
                fillContent(serviceElement, children, serviceId, operationId);

                try {
                    OutputFormat format = OutputFormat.createPrettyPrint();
                    format.setEncoding("utf-8");
                    FileOutputStream fos = new FileOutputStream(fileName);
                    XMLWriter writer = new XMLWriter(fos, format);
                    writer.write(doc);
                    writer.close();
                } catch (IOException e) {
                    log.error(e, e);
                }
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    /**
     * 生成服务定义文件
     *
     * @param serviceInvoke
     * @param path
     * @return
     */
    @Override
    public void genrateServiceFile(ServiceInvoke serviceInvoke, String path) {
        try {
            String serviceId = serviceInvoke.getServiceId();
            String operationId = serviceInvoke.getOperationId();
            Operation operation = operationService.getOperation(serviceId, operationId);
            com.dc.esb.servicegov.entity.System system = serviceInvoke.getSystem();
            String fileName = path + File.separator + "service_" + serviceId + operationId + ".xml";
            File file = new File(fileName);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            Document doc = DocumentHelper.createDocument();
            Element rootElement = doc.addElement("S" + serviceId + operationId);//根节点
            Element requestElement = rootElement.addElement("request");
            Element reqSdoRoottElement = requestElement.addElement("sdoroot");
            Element responseElement = rootElement.addElement("response");
            Element resSdoRoottElement = responseElement.addElement("sdoroot");
//            fillServiceHead(operation, serviceInvoke.getInterfaceId(), reqSdoRoottElement, Constants.ElementAttributes.REQUEST_NAME, true);
//            fillServiceHead(operation, serviceInvoke.getInterfaceId(),resSdoRoottElement, Constants.ElementAttributes.RESPONSE_NAME, true);

            //手动写报文头 request端
            Element sys_head_req = reqSdoRoottElement.addElement("SYS_HEAD");
            Element SvcId = sys_head_req.addElement("SvcId");
            SvcId.addAttribute("metadataid", "SvcId");
            Element SvcScnId = sys_head_req.addElement("SvcScnId");
            SvcScnId.addAttribute("metadataid", "SvcScnId");
            Element CnsmSysId = sys_head_req.addElement("CnsmSysId");
            CnsmSysId.addAttribute("metadataid", "CnsmSysId");
            Element ChnlType = sys_head_req.addElement("ChnlType");
            ChnlType.addAttribute("metadataid", "ChnlType");
            Element SrcSysId = sys_head_req.addElement("SrcSysId");
            SrcSysId.addAttribute("metadataid", "SrcSysId");
            Element CnsmSysSeqNo = sys_head_req.addElement("CnsmSysSeqNo");
            CnsmSysSeqNo.addAttribute("metadataid", "CnsmSysSeqNo");
            Element SrcSysSeqNo = sys_head_req.addElement("SrcSysSeqNo");
            SrcSysSeqNo.addAttribute("metadataid", "SrcSysSeqNo");
            Element Mac = sys_head_req.addElement("Mac");
            Mac.addAttribute("metadataid", "Mac");
            Element MacOrgId = sys_head_req.addElement("MacOrgId");
            MacOrgId.addAttribute("metadataid", "MacOrgId");
            Element TranMode = sys_head_req.addElement("TranMode");
            TranMode.addAttribute("metadataid", "TranMode");
            Element TranDate = sys_head_req.addElement("TranDate");
            TranDate.addAttribute("metadataid", "TranDate");
            Element TranTime = sys_head_req.addElement("TranTime");
            TranTime.addAttribute("metadataid", "TranTime");
            Element TmnlNo = sys_head_req.addElement("TmnlNo");
            TmnlNo.addAttribute("metadataid", "TmnlNo");
            Element SrcSySysnlNo = sys_head_req.addElement("SrcSySysnlNo");
            SrcSySysnlNo.addAttribute("metadataid", "SrcSySysnlNo");
            Element CnsmSysSvrId = sys_head_req.addElement("CnsmSysSvrId");
            CnsmSysSvrId.addAttribute("metadataid", "CnsmSysSvrId");
            Element SrcSysSvrId = sys_head_req.addElement("SrcSysSvrId");
            SrcSysSvrId.addAttribute("metadataid", "SrcSysSvrId");
//            Element UserLang=sys_head_req.addElement("UserLang");
//            UserLang.addAttribute("metadataid","UserLang");

            Element app_head_req = reqSdoRoottElement.addElement("APP_HEAD");
            Element TellerId = app_head_req.addElement("TellerId");
            TellerId.addAttribute("metadataid", "TellerId");
            Element BranchId = app_head_req.addElement("BranchId");
            BranchId.addAttribute("metadataid", "BranchId");
            Element TlrPswd = app_head_req.addElement("TlrPswd");
            TlrPswd.addAttribute("metadataid", "TlrPswd");
            Element TlrLvl = app_head_req.addElement("TlrLvl");
            TlrLvl.addAttribute("metadataid", "TlrLvl");
            Element TlrType = app_head_req.addElement("TlrType");
            TlrType.addAttribute("metadataid", "TlrType");
            Element ChkFlag = app_head_req.addElement("ChkFlag");
            ChkFlag.addAttribute("metadataid", "ChkFlag");
            Element AuthFlag = app_head_req.addElement("AuthFlag");
            AuthFlag.addAttribute("metadataid", "AuthFlag");
            Element AuthTlrId = app_head_req.addElement("AuthTlrId");
            AuthTlrId.addAttribute("metadataid", "AuthTlrId");
//            Element AuthBrId=app_head_req.addElement("AuthBrId");
//            AuthBrId.addAttribute("metadataid","AuthBrId");
            Element AuthBrId = app_head_req.addElement("AuthBrchId");
            AuthBrId.addAttribute("metadataid", "AuthBrchId");
            Element AuthTlrPswd = app_head_req.addElement("AuthTlrPswd");
            AuthTlrPswd.addAttribute("metadataid", "AuthTlrPswd");

            //手动写报文头 response端
            Element sys_head_res = resSdoRoottElement.addElement("SYS_HEAD");
            Element SvcId1 = sys_head_res.addElement("SvcId").addAttribute("metadataid", "SvcId");
            Element SvcScnId1 = sys_head_res.addElement("SvcScnId").addAttribute("metadataid", "SvcScnId");
            Element CnsmSysId1 = sys_head_res.addElement("CnsmSysId").addAttribute("metadataid", "CnsmSysId");
            Element PrvdSysId = sys_head_res.addElement("PrvdSysId").addAttribute("metadataid", "PrvdSysId");
            Element CnsmSysSeqNo1 = sys_head_res.addElement("CnsmSysSeqNo").addAttribute("metadataid", "CnsmSysSeqNo");
            Element PrvdSysSeqNo = sys_head_res.addElement("PrvdSysSeqNo").addAttribute("metadataid", "PrvdSysSeqNo");
            Element Mac1 = sys_head_res.addElement("Mac").addAttribute("metadataid", "Mac");
            Element MacOrgId1 = sys_head_res.addElement("MacOrgId").addAttribute("metadataid", "MacOrgId");
            Element TranDate1 = sys_head_res.addElement("TranDate").addAttribute("metadataid", "TranDate");
            Element TranTime1 = sys_head_res.addElement("TranTime").addAttribute("metadataid", "TranTime");
            Element TranRetSt = sys_head_res.addElement("TranRetSt").addAttribute("metadataid", "TranRetSt");
            Element array = sys_head_res.addElement("array");
            Element RetInf = array.addElement("RetInf").addAttribute("metadataid", "RetInf").addAttribute("type", "array").addAttribute("is_struct", "false");
            Element sdo = RetInf.addElement("sdo");
            Element RetCode = sdo.addElement("RetCode").addAttribute("metadataid", "RetCode");
            Element RetMsg = sdo.addElement("RetMsg").addAttribute("metadataid", "RetMsg");
            Element PrvdSysSvrId = sys_head_res.addElement("PrvdSysSvrId").addAttribute("metadataid", "PrvdSysSvrId");
//            Element UserLang1=sys_head_res.addElement("UserLang").addAttribute("metadataid", "UserLang");

            Element app_head_res = resSdoRoottElement.addElement("APP_HEAD");
            Element TellerId1 = app_head_res.addElement("TellerId").addAttribute("metadataid", "TellerId");
            Element BranchId1 = app_head_res.addElement("BranchId").addAttribute("metadataid", "BranchId");
            Element TlrPswd1 = app_head_res.addElement("TlrPswd").addAttribute("metadataid", "TlrPswd");
            Element TlrLvl1 = app_head_res.addElement("TlrLvl").addAttribute("metadataid", "TlrLvl");
            Element TlrType1 = app_head_res.addElement("TlrType").addAttribute("metadataid", "TlrType");
            Element ChkFlag1 = app_head_res.addElement("ChkFlag").addAttribute("metadataid", "ChkFlag");
            Element AuthFlag1 = app_head_res.addElement("AuthFlag").addAttribute("metadataid", "AuthFlag");
            Element AuthTlrId1 = app_head_res.addElement("AuthTlrId").addAttribute("metadataid", "AuthTlrId");
//            Element AuthBrId1 =app_head_res.addElement("AuthBrId").addAttribute("metadataid","AuthBrId");
            Element AuthBrId1 = app_head_res.addElement("AuthBrchId").addAttribute("metadataid", "AuthBrchId");

            Element reqBody = reqSdoRoottElement.addElement("BODY");
            reqBody.addElement("TranCode").addAttribute("metadataid", "TranCode");
            reqBody.addElement("SessionId").addAttribute("metadataid", "SessionId");
            reqBody.addElement("SubBnkId").addAttribute("metadataid", "SubBnkId");
            fillBodyInside(operation, serviceInvoke.getInterfaceId(), reqBody, Constants.ElementAttributes.REQUEST_NAME, true);

            Element resBody = resSdoRoottElement.addElement("BODY");
            resBody.addElement("CnsmSysDt").addAttribute("metadataid", "CnsmSysDt");
            fillBodyInside(operation, serviceInvoke.getInterfaceId(), resBody, Constants.ElementAttributes.RESPONSE_NAME, true);

            try {
                OutputFormat format = OutputFormat.createPrettyPrint();
                format.setEncoding("utf-8");
                FileOutputStream fos = new FileOutputStream(fileName);
                XMLWriter writer = new XMLWriter(fos, format);
                writer.write(doc);
                writer.close();
            } catch (IOException e) {
                log.error(e, e);
            }
        } catch (Exception e) {
            log.error("生成服务定义文件失败！", e);
        }
    }

    /**
     * 填充
     */
    public void fillContent(Element element, List<Ida> idas, String serviceId, String operationId) {

        Document ISO8583DOC = referXmlIso8583();
        //加F60_1 父节点 F60

        for (Ida ida : idas) {

            if(ida.getStructName().equals("bitmap")){
                Element idaElement = null;
                if( flag==1){
                    element.addElement("bitmap").addAttribute("metadataid","IB1").addAttribute("type","bitmap");
                }else if(flag==2){
                    element.addElement("IB1").addAttribute("metadataid","IB1").addAttribute("type","bitmap");
                    element.addElement("IB2").addAttribute("metadataid","IB2").addAttribute("type", "bitmap");
                }
                continue;
            }

            String name = ida.getStructName();
            String[] array = name.split("_");
            Element idaElement;
            Element parentEle = null;
            if (array.length == 2 && array[1].equals("1")) {
                parentEle = ISO8583DOC.getRootElement().element(array[0]);
                element.addElement(array[0]);
                element.element(array[0]).appendAttributes(parentEle);
            }
            parentEle = element.element(array[0]);
//            ida.setLength(ISO8583DOC.getRootElement().element(ida.getStructName()).attributeValue("length"));
            if (array.length > 1 && element.element(array[0]) != null) {
                idaElement = fillContentTag(parentEle, ida, serviceId, operationId);
            } else {
                idaElement = fillContentTag(element, ida, serviceId, operationId);
            }
            List<Ida> children = idaService.getNotEmptyByParentId(ida.getId());
            fillContent(idaElement, children, serviceId, operationId);
        }
    }

    public Element fillContentTag(Element element, Ida ida, String serviceId, String operationId) {
        Element idaElement = element.addElement(ida.getStructName());
        Map<String, String> params = new HashMap<String, String>();
        params.put("serviceId", serviceId);
        params.put("operationId", operationId);
        params.put("xpath", ida.getXpath());
        SDA sda = sdaService.findUniqueBy(params);
        addAttribute(idaElement, "type", "string");
        addAttribute(idaElement, "length", findLength(ida.getType()));
        String type = ida.getType();
        if (type.indexOf("(LLVAR)") > 0) {
            addAttribute(idaElement, "variable_flag", "2");
        } else if (type.indexOf("(LLLVAR)") > 0) {
            addAttribute(idaElement, "variable_flag", "3");
        }
        if (null != sda) {
            addAttribute(idaElement, "metadataid", sda.getMetadataId());
        }
        addAttribute(idaElement, "encoding", "GBK");
        if ("array".equalsIgnoreCase(ida.getType())) {
            addAttribute(idaElement, "type", "array");
            addAttribute(idaElement, "is_struct", "false");
        }
//        addAttribute(idaElement, "chinese_name", ida.getStructAlias());
        return idaElement;
    }

    //获取参考xml文件
    public Document referXmlIso8583(){
        ClassLoader loader = this.getClass().getClassLoader();
        String file = loader.getResource("iso8583_template/ISO8583medata.xml").getPath();

        Document ISO8583DOC = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(file)),
                    "utf-8"));
            String temp = null;
            StringBuffer xml = new StringBuffer();
            try {
                while ((temp = reader.readLine()) != null) {
                    xml.append(temp);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            ISO8583DOC = DocumentHelper.parseText(xml.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return ISO8583DOC;
    }

    //根据类型获取长度
    public String findLength(String type){
        StringBuffer num=new StringBuffer();
        for(int i=0;i<type.length();i++){
            if(type.charAt(i)>=48 && type.charAt(i)<=57){
                num.append(type.charAt(i));
            }
        }
        return num.toString();
    }
}
