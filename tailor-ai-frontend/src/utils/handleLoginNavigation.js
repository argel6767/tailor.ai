import getHasSetProfession from "../api/user/getHasSetProfession.js";

/**
 * handles the navigation logic of when a user logins
 * if the user has yet to set their profession (ie a new user) they will be routed and prompted to do so
 * else the user has set their profession so they routed to /chats to see their chats dashboard
 */
export const handleLoginNavigation = async (navigate, email, token) => {
    const hasSetProfession = await getHasSetProfession(email, token);
    hasSetProfession ? goToChatDashBoardPage(navigate) : goToProfessionPage(navigate)

}

/**
 * navigates to chat dashboard page
 */
const goToChatDashBoardPage = (navigate) => {
    navigate("/chats");
}

/**
 * navigates to setting profession page
 */
const goToProfessionPage = (navigate) => {
    navigate("/profession");
}
