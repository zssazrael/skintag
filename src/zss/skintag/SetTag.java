package zss.skintag;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.jsp.JspException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import zss.tool.LoggedException;
import zss.tool.Version;
import zss.tool.web.AbstractBodyTag;

@Version("2016-03-30")
public class SetTag extends AbstractBodyTag
{
    private static final Logger LOGGER = LoggerFactory.getLogger(SetTag.class);

    private String name;

    public void setName(final String name)
    {
        this.name = name;
    }

    @Override
    public void release()
    {
        name = null;
        super.release();
    }

    @Override
    public void doInitBody() throws JspException
    {
    }

    @Override
    public int doAfterBody() throws JspException
    {
        final StringWriter writer = new StringWriter(4096);
        try
        {
            bodyContent.writeOut(writer);
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
        final LayoutTag tag = (LayoutTag) getParent();
        tag.setContent(name, writer.toString());
        return SKIP_BODY;
    }

    @Override
    public int doStartTag() throws JspException
    {
        if (getParent() instanceof LayoutTag)
        {
            return EVAL_BODY_BUFFERED;
        }
        return SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspException
    {
        return EVAL_PAGE;
    }
}
