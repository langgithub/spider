package com.champion.spider.selector;

import cn.wanghaomiao.xpath.core.XpathEvaluator;
import cn.wanghaomiao.xpath.exception.NoSuchAxisException;
import cn.wanghaomiao.xpath.exception.NoSuchFunctionException;
import cn.wanghaomiao.xpath.model.JXNode;
import org.apache.commons.collections.CollectionUtils;
import org.jsoup.nodes.Element;

import java.util.LinkedList;
import java.util.List;

/**
 * XPath selector based on Xsoup.<br>
 *
 * @author xulixiang@champion-credit.com
 * @version 1.0.0
 * @date 2016.5.28
 */
public class XpathSelector extends BaseElementSelector {


    private XpathEvaluator xPathEvaluator;
    private String xpath;

    public XpathSelector(String xpth) {
        this.xPathEvaluator = new XpathEvaluator();
        this.xpath=xpth;
    }

    @Override
    public String select(Element element) {
        List<JXNode> jns=null;
        try {
            jns= xPathEvaluator.xpathParser(xpath,element.children());
        } catch (NoSuchAxisException e) {
            e.printStackTrace();
        } catch (NoSuchFunctionException e) {
            e.printStackTrace();
        }
        return jns.get(0).getTextVal();
    }

    @Override
    public List<String> selectList(Element element) {
        List<String> res = new LinkedList<String>();
        try {
            List<JXNode> jns = xPathEvaluator.xpathParser(xpath,element.children());
            for (JXNode j:jns){
                if (j.isText()){
                    res.add(j.getTextVal());
                }
            }
        } catch (Exception e){
            String msg = "please check the xpath syntax";
            if (e instanceof NoSuchAxisException||e instanceof NoSuchFunctionException){
                msg = e.getMessage();
            }
        }
        return res;
    }

    @Override
    public Element selectElement(Element element) {
        List<Element> elements = selectElements(element);
        if (CollectionUtils.isNotEmpty(elements)){
            return elements.get(0);
        }
        return null;
    }

    @Override
    public List<Element> selectElements(Element element) {
        List<Element> res = new LinkedList<Element>();
        try {
            List<JXNode> jns = xPathEvaluator.xpathParser(xpath,element.children());
            for (JXNode j:jns){
                if (!j.isText()){
                    res.add(j.getElement());
                }
            }
        } catch (Exception e){
            String msg = "please check the xpath syntax";
            if (e instanceof NoSuchAxisException||e instanceof NoSuchFunctionException){
                msg = e.getMessage();
            }
        }
        return res;
    }

    @Override
    public boolean hasAttribute() {
        return true;
    }
}
