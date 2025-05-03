import {useParams} from "react-router-dom";
import Loading from "./Loading.jsx";
import {useEffect, useState} from "react";
import getPdfFile from "../api/s3/getPdfFile.js";

// Cache object to store PDF URLs by chat ID
const pdfCache = new Map();

export const FilePopUp = ({closePopUp}) => {
    const {id} = useParams();
    const [fileUrl, setFileUrl] = useState(null);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        // Reset states when ID changes
        setIsLoading(true);
        setFileUrl(null);

        // Check cache for this specific ID
        if (pdfCache.has(id)) {
            setFileUrl(pdfCache.get(id));
            setIsLoading(false);
            return;
        }

        const grabFile = async () => {
            try {
                console.log('Fetching PDF for chat ID:', id); // Debug log
                const response = await getPdfFile(id, );
                const url = URL.createObjectURL(response);
                pdfCache.set(id, url);
                setFileUrl(url);
            } catch (error) {
                console.error('Error loading file for chat ID:', id, error);
            } finally {
                setIsLoading(false);
            }
        };

        grabFile();
    }, [id, ]); // Dependencies include id to react to chat changes


    return (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-start justify-center pt-4">
            <div className="p-6 rounded shadow-lg flex flex-col gap-5 bg-background w-9/12">
                {isLoading ?
                    <Loading loadingMessage={"Grabbing file..."}/> :
                    <div className="flex flex-col gap-6">
                        {fileUrl && <iframe className="h-[45rem]" src={fileUrl} title={`Resume for chat ${id}`}></iframe>}
                        <button className="btn btn-primary" onClick={closePopUp}>Close
                        </button>
                    </div>
                }
            </div>
        </div>
    );
};