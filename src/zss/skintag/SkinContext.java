package zss.skintag;

import org.apache.commons.lang.StringUtils;

import zss.tool.Version;

@Version("2015-04-27")
public class SkinContext
{
    private static String folder = "/WEB-INF/skin";

    public static String getFolder()
    {
        return folder;
    }

    public static void setFolder(final String folder)
    {
        if (StringUtils.isEmpty(folder))
        {
            SkinContext.folder = "/WEB-INF/skin";
        }
        else
        {
            final StringBuilder builder = new StringBuilder();
            if (folder.charAt(0) != '/')
            {
                builder.append('/');
            }
            builder.append(folder);
            while (true)
            {
                final int length = builder.length() - 1;
                if (builder.charAt(length) == '/')
                {
                    builder.setLength(length);
                }
                else
                {
                    break;
                }
            }
            SkinContext.folder = builder.toString();
        }
    }
}
