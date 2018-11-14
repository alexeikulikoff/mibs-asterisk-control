package mibs.asterisk.control.utils;

import java.security.Principal;
import java.util.Objects;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class AnonymousPrincipal implements Principal{

	private String name;
	@Override
	public String getName() {
		return name;
	}
	public void setName(String name) {
        this.name = name;
    }
    @Override
    public boolean equals(Object another) {
        if (!(another instanceof Principal))
            return false;
        Principal principal = (Principal) another;
        return principal.getName() == this.name;

    }
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
    @Override
    public String toString() {
    	return ToStringBuilder.reflectionToString(this);
    }
}
