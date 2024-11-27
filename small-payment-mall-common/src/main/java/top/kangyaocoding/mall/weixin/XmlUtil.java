package top.kangyaocoding.mall.weixin;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述: 解析微信 xml 工具类
 *
 * @author K·Herbert
 * @since 2024-11-27 下午10:02
 */
public class XmlUtil {
    /**
     * 解析微信发来的请求(xml)
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> xmlToMap(HttpServletRequest request) throws Exception {
        // 从request中取得输入流
        try (InputStream inputStream = request.getInputStream()) {
            // 将解析结果存储在HashMap中
            Map<String, String> map = new HashMap<>();
            // 读取输入流
            SAXReader reader = new SAXReader();
            // 得到xml文档
            Document document = reader.read(inputStream);
            // 得到xml根元素
            Element root = document.getRootElement();
            // 得到根元素的所有子节点
            List<Element> elementList = root.elements();
            // 遍历所有子节点
            for (Element e : elementList)
                map.put(e.getName(), e.getText());
            // 释放资源
            inputStream.close();
            return map;
        }
    }

    /**
     * 将map转化成xml响应给微信服务器
     */
    public static String mapToXML(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        mapToXML2(map, sb);
        sb.append("</xml>");
        return sb.toString();
    }

    private static void mapToXML2(Map<String, Object> map, StringBuilder sb) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value == null) {
                value = "";
            }
            if (value instanceof List) {
                sb.append("<").append(key).append(">");
                for (Object item : (List<?>) value) {
                    if (item instanceof Map) {
                        mapToXML2((Map<String, Object>) item, sb);
                    }
                }
                sb.append("</").append(key).append(">");
            } else if (value instanceof Map) {
                sb.append("<").append(key).append(">");
                mapToXML2((Map<String, Object>) value, sb);
                sb.append("</").append(key).append(">");
            } else {
                sb.append("<").append(key).append("><![CDATA[").append(value).append("]]></").append(key).append(">");
            }
        }
    }

    public static XStream getMyXStream() {
        return new XStream(new XppDriver() {
            @Override
            public HierarchicalStreamWriter createWriter(Writer out) {
                return new PrettyPrintWriter(out) {
                    // 对所有xml节点都增加CDATA标记
                    final boolean cdata = true;

                    @Override
                    public void startNode(String name, Class clazz) {
                        super.startNode(name, clazz);
                    }

                    @Override
                    protected void writeText(QuickWriter writer, String text) {
                        if (cdata && !StringUtils.isNumeric(text)) {
                            writer.write("<![CDATA[");
                            writer.write(text);
                            writer.write("]]>");
                        } else {
                            writer.write(text);
                        }
                    }
                };
            }
        });
    }

    /**
     * bean转成微信的xml消息格式
     */
    public static String beanToXml(Object object) {
        XStream xStream = getMyXStream();
        xStream.alias("xml", object.getClass());
        xStream.processAnnotations(object.getClass());
        return xStream.toXML(object);
    }

    /**
     * xml转成bean泛型方法
     */
    public static <T> T xmlToBean(String resultXml, Class<T> clazz) {
        XStream stream = new XStream(new DomDriver());
        XStream.setupDefaultSecurity(stream);
        stream.allowTypes(new Class[]{clazz});
        stream.processAnnotations(clazz);
        stream.setMode(XStream.NO_REFERENCES);
        stream.alias("xml", clazz);

        return clazz.cast(stream.fromXML(resultXml));
    }
}
