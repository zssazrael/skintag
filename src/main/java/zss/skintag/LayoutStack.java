package zss.skintag;

import java.util.LinkedList;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.PageContext;

import zss.tool.Version;
import zss.tool.web.AbstractBodyTag;
import zss.tool.web.AbstractTag;

@Version("2018.09.23")
public class LayoutStack extends LinkedList<LayoutTag> {
    private static final long serialVersionUID = 20160330230332873L;

    public static LayoutStack getInstance(final AbstractTag tag) {
        final PageContext pageContext = tag.getPageContext();
        return getInstance(pageContext);
    }

    public static LayoutStack getInstance(final AbstractBodyTag tag) {
        final PageContext pageContext = tag.getPageContext();
        return getInstance(pageContext);
    }

    public static LayoutStack getInstance(final PageContext pageContext) {
        final ServletRequest request = pageContext.getRequest();
        return getInstance(request);
    }

    public static LayoutStack getInstance(final ServletRequest request) {
        final Object object = request.getAttribute(LayoutStack.class.getName());
        if (object instanceof LayoutStack) {
            return (LayoutStack) object;
        }
        final LayoutStack layoutStack = new LayoutStack();
        request.setAttribute(LayoutStack.class.getName(), layoutStack);
        return layoutStack;
    }

    private LayoutStack() {
    }
}
