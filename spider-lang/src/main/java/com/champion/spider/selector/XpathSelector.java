package com.champion.spider.selector;

import org.jsoup.nodes.Element;

import java.util.List;

/**
 * XPath selector based on Xsoup.<br>
 *
 * @author xulixiang@champion-credit.com
 * @version 1.0.0
 * @date 2016.5.28
 */
public class XpathSelector extends BaseElementSelector {
    public XpathSelector(String xpathStr) {
//        this.xPathEvaluator = Xsoup.compile(xpathStr);
    }
    @Override
    public Element selectElement(Element element) {
        return null;
    }

    @Override
    public List<Element> selectElements(Element element) {
        return null;
    }

    @Override
    public boolean hasAttribute() {
        return false;
    }

    @Override
    public String select(Element element) {
        return null;
    }

    @Override
    public List<String> selectList(Element element) {
        return null;
    }

////    private XPathEvaluator xPathEvaluator;
//
//    public XpathSelector(String xpathStr) {
////        this.xPathEvaluator = Xsoup.compile(xpathStr);
//    }
//
//    @Override
//    public String select(Element element) {
////        return xPathEvaluator.evaluate(element).get();
//    }
//
//    @Override
//    public List<String> selectList(Element element) {
//        return xPathEvaluator.evaluate(element).list();
//    }
//
//    @Override
//    public Element selectElement(Element element) {
//        List<Element> elements = selectElements(element);
//        if (CollectionUtils.isNotEmpty(elements)){
//            return elements.get(0);
//        }
//        return null;
//    }
//
//    @Override
//    public List<Element> selectElements(Element element) {
//        return xPathEvaluator.evaluate(element).getElements();
//    }
//
//    @Override
//    public boolean hasAttribute() {
//        return xPathEvaluator.hasAttribute();
//    }
}
