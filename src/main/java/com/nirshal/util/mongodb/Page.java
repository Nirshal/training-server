package com.nirshal.util.mongodb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.QueryParam;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Page {

    public static final String ASC = "asc";
    public static final String DESC = "desc";

//    @HeaderParam(RestConstants.HEADER_SOCIAL_BETTING_USER_ID)
//    private String socialBettingUserId;

    /**
     * requested page (0 based)
     */
//    @QueryParam(RestConstants.PARAM_PAGE)
//    @Min(0)
//    @DefaultValue("0")
    Integer page = 0;

    /**
     * number of available pages (BE ignores this field. it is made for sending to the FE)
     */
//    @QueryParam(RestConstants.PARAM_PAGES_NUMBER)
//    @Min(0)
//    @DefaultValue("0")
    Integer pagesNumber = 0;

    /**
     * number of item per page
     */
//    @QueryParam(RestConstants.PARAM_ITEM_PER_PAGE)
//    @Positive
//    @DefaultValue("10")
    Integer itemPerPage = 10;

    /**
     * sort by field name
     */
//    @QueryParam(RestConstants.PARAM_SORT_BY)
    String sortBy;

    /**
     * sort direction accepted values: asc | desc
     */
//    @QueryParam(RestConstants.PARAM_SORT_DIRECTION)
//    @Pattern(regexp = "asc|desc", flags = Pattern.Flag.CASE_INSENSITIVE)
//    @DefaultValue("DESC")  // default DESC
    String sortDirection = "DESC";

//    public int getElasticFrom() {
//    	return this.page * this.itemPerPage;
//    }
//
//
//    public int getElasticTo() {
//        return this.getElasticFrom() + this.itemPerPage;
//    }
//
//
//    public int getPageOffset() {
//        return this.itemPerPage * this.page;
//    }
}
