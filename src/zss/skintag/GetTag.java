package zss.skintag;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import zss.tool.LoggedException;
import zss.tool.Version;
import zss.tool.web.AbstractTag;

@Version("2016-03-30")
public class GetTag extends AbstractTag
{
    private static final Logger LOGGER = LoggerFactory.getLogger(GetTag.class);

    private String name;
    private String variable;
    private String stack;

    public void setStack(String stack)
    {
        this.stack = stack;
    }

    public void setVariable(final String variable)
    {
        this.variable = variable;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    @Override
    public void release()
    {
        variable = null;
        name = null;
        super.release();
    }

    @Override
    public int doEndTag() throws JspException
    {
        final LayoutTag tag = LayoutTag.getLayout(stack);
        if (tag != null)
        {
            final String content = StringUtils.defaultString(tag.getContent(name));
            if (StringUtils.isEmpty(variable))
            {
                try
                {
                    pageContext.getOut().write(content);
                }
                catch (IOException e)
                {
                    LOGGER.error(e.getMessage(), e);
                    throw new LoggedException();
                }
            }
            else
            {
                pageContext.setAttribute(variable, content);
            }
        }
        return EVAL_PAGE;
    }

    @Override
    public int doStartTag() throws JspException
    {
        return SKIP_BODY;
    }
}
