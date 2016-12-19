package zss.skintag;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.jsp.JspException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import zss.tool.LoggedException;
import zss.tool.Version;
import zss.tool.web.AbstractBodyTag;

@Version("2016-03-30")
public class LayoutTag extends AbstractBodyTag
{
    private static final Logger LOGGER = LoggerFactory.getLogger(LayoutTag.class);
    private static final StackLocal STACK = new StackLocal();

    private final ContentMap contents = new ContentMap();

    private String name;
    private String set;

    static LayoutTag getLayout(final String stack)
    {
        final LayoutStack list = STACK.get();
        if (list.isEmpty())
        {
            return null;
        }
        int index = NumberUtils.toInt(stack, 0);
        index = Math.max(0, index);
        index = Math.min(index, list.size() - 1);
        return list.get(index);
    }

    public void setSet(final String set)
    {
        this.set = set;
    }

    void setContent(final String name, final String content)
    {
        contents.put(name, content);
    }

    String getContent(final String name)
    {
        return contents.get(name);
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    @Override
    public void doInitBody() throws JspException
    {
    }

    @Override
    public int doAfterBody() throws JspException
    {
        try
        {
            if (StringUtils.isNotEmpty(set))
            {
                final StringWriter writer = new StringWriter(4096);
                bodyContent.writeOut(writer);
                setContent(set, writer.toString());
            }
        }
        catch (IOException e)
        {
            LOGGER.error(e.getMessage(), e);
            throw new LoggedException();
        }
        finally
        {
            bodyContent.clearBody();
        }
        return SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspException
    {
        try
        {
            pageContext.include(String.format("%s/%s.jsp", SkinContext.getFolder(), name));
        }
        catch (ServletException e)
        {
            LOGGER.error(e.getMessage(), e);
            throw new LoggedException();
        }
        catch (IOException e)
        {
            LOGGER.error(e.getMessage(), e);
            throw new LoggedException();
        }
        final LayoutStack stack = STACK.get();
        stack.removeFirst();
        if (stack.isEmpty())
        {
            STACK.remove();
        }
        return EVAL_PAGE;
    }

    @Override
    public int doStartTag() throws JspException
    {
        STACK.get().addFirst(this);
        return EVAL_BODY_BUFFERED;
    }

    @Override
    public void release()
    {
        contents.clear();
        name = null;
        set = null;
        super.release();
    }

    @Version("2013-12-25")
    private static class ContentMap extends HashMap<String, String>
    {
        private static final long serialVersionUID = 20131225222128432L;
    }

    @Version("2016-03-30")
    private static class StackLocal extends ThreadLocal<LayoutStack>
    {
        @Override
        protected LayoutStack initialValue()
        {
            return new LayoutStack();
        }
    }

    @Version("2016-03-30")
    private static class LayoutStack extends LinkedList<LayoutTag>
    {
        private static final long serialVersionUID = 20160330230332873L;
    }
}
