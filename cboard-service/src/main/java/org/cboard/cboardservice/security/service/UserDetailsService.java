package org.cboard.cboardservice.security.service;

import org.cboard.cboardservice.modules.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.cas.userdetails.AbstractCasAssertionUserDetailsService;
import org.springframework.util.Assert;

@SuppressWarnings("deprecation")
//public final class UserDetailsService extends AbstractCasAssertionUserDetailsService {
public final class UserDetailsService  {

    private static final String NON_EXISTENT_PASSWORD_VALUE = "NO_PASSWORD";

    @SuppressWarnings("unused")
    private final String[] attributes;

    @SuppressWarnings("unused")
    private boolean convertToUpperCase = true;

    @Autowired
    private UserDao userDao;

    public UserDetailsService(final String[] attributes) {
        Assert.notNull(attributes, "attributes cannot be null.");
        Assert.isTrue(attributes.length > 0, "At least one attribute is required to retrieve roles from.");
        this.attributes = attributes;
    }

    @SuppressWarnings("unchecked")
//    @Override
//    protected UserDetails loadUserDetails(final Assertion assertion) {
//        final List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
//        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
//        User user = new User(
//                (String) assertion.getPrincipal().getAttributes().get("displayName"),
//                NON_EXISTENT_PASSWORD_VALUE,
//                true, true, true, true,
//                grantedAuthorities);
//        user.setCompany((String) assertion.getPrincipal().getAttributes().get("company"));
//        user.setDepartment((String) assertion.getPrincipal().getAttributes().get("department"));
//        user.setUserId((String) assertion.getPrincipal().getAttributes().get("employee"));
//        user.setName(assertion.getPrincipal().getName());
//        userDao.saveNewUser(user.getUserId(), user.getUsername(), user.getName());
//        return user;
//    }

    /**
     * Converts the returned attribute values to uppercase values.
     *
     * @param convertToUpperCase true if it should convert, false otherwise.
     */
    public void setConvertToUpperCase(final boolean convertToUpperCase) {
        this.convertToUpperCase = convertToUpperCase;
    }
}
