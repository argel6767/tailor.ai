import getHasSetProfession from "../api/getHasSetProfession.js";
import {useNavigate} from "react-router-dom";

const navigate = useNavigate()

/**
 * handles the navigation logic of when a user logins
 * if the user has yet to set their profession (ie a new user) they will be routed and prompted to do so
 * else the user has set their profession so they routed to /chats to see their chats dashboard
 */
export const handleLoginNavigation = async () => {
    const hasSetProfession = await getHasSetProfession();
    hasSetProfession ? goToChatDashBoardPage() : goToProfessionPage()

}

/**
 * navigates to chat dashboard page
 */
const goToChatDashBoardPage = () => {
    navigate("/chats");
}

/**
 * navigates to setting profession page
 */
const goToProfessionPage = () => {
    navigate("/profession");
}
