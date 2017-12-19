package zss.skintag;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import zss.tool.LoggedException;
import zss.tool.Version;
import zss.tool.web.AbstractBodyTag;

@Version("2018.09.23")
public class SetTag extends AbstractBodyTag
{
    private static final Logger LOGGER = LoggerFactory.getLogger(SetTag.class);

    private String name;
    private String from;
    private String content;
    private int stack;

    public void setFrom(String from) {
        this.from = from;
    }

    public void setStack(int stack) {
        this.stack = stack;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    @Override
    public void release() {
        name = null;
        from = null;
        content = null;
        stack = 0;
        super.release();
    }

    @Override
    public void doInitBody() throws JspException
    {
    }

    @Override
    public int doAfterBody() throws JspException {
        final StringWriter writer = new StringWriter(4096);
        try {
            bodyContent.writeOut(writer);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new LoggedException();
        } finally {
            bodyContent.clearBody();
        }
        content = writer.toString();
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
    public int doEndTag() throws JspException {
        final LayoutTag tag = (LayoutTag) getParent();
        if (StringUtils.isEmpty(from)) {
            tag.setContent(name, StringUtils.defaultString(content));
        } else {
            final LayoutTag fromTag = LayoutStack.getInstance(this).get(stack);
            if (fromTag != null) {
                final String content = StringUtils.defaultString(fromTag.getContent(from));
                tag.setContent(name, content);
            }
        }
        return EVAL_PAGE;
    }
}
