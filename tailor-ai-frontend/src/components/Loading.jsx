const Loading = (loadingMessage) => {
    return (
        <span className="flex flex-col items-center justify-center h-full gap-3 px-2">
            <span className="loading loading-spinner w-1/2"></span>
            <p>Grabbing your previous chats...</p>
        </span>

    )
}

export default Loading