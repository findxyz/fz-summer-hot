package xyz.fz.model;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Created by Administrator on 2017/5/8.
 */
public class XmlAdapterCDATA extends XmlAdapter<String, String> {

    @Override
    public String marshal(String arg0) throws Exception {
        return "<![CDATA[" + arg0 + "]]>";
    }

    @Override
    public String unmarshal(String arg0) throws Exception {
        return arg0;
    }
}
