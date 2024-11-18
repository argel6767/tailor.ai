
const Sidebar = () => {
    return (
        <div className="drawer lg:drawer-open">
            <input id="my-drawer-2" type="checkbox" className="drawer-toggle"/>
            <div className="drawer-content flex flex-col items-center justify-center">
                {/* Page content here */}
                <label htmlFor="my-drawer-2" className="btn btn-primary drawer-button lg:hidden">
                    Open drawer
                </label>
            </div>
            <div className="drawer-side bg-base-300 pl-2">
                <h1 className="text-2xl text-center pt-1">View previous chats below.</h1>
                <label htmlFor="my-drawer-2" aria-label="close sidebar" className="drawer-overlay"></label>
                <ul className="menu bg-base-300 text-base-content min-h-full w-80 p-4">
                    {/*TODO dynamically make list of chat sessions gotten by the api request*/}
                    {/*TODO also make a loading placeholder for them until they load up eventually*/}
                    <li><a>Sidebar Item 1</a></li> {/* name will be either the name set or just the default name*/}
                    <li><a>Sidebar Item 2</a></li>
                </ul>
            </div>
        </div>
    )
}

export default Sidebar;