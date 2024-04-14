import React, {useContext, useEffect} from "react";
import Header from "../../components/AboutMe";
import InputFileds from "../../components/InputFileds/InputFileds";
import Graph from "../../components/Graph";
import {CoordinatesProvider, DotsFormContext} from "../../components/InputFileds/InputFieldContext";
import DotsTable from "../../components/DotsTable";
import {AuthorizationStore} from "../../redux/authorizationStore";
import {useNavigate} from "react-router-dom";
import {getLogin} from "../../redux/login";
import dotsTable from "../../components/DotsTable";
import {getPassword} from "../../redux/password";
import toast from "react-hot-toast";
import HeaderNav from "../../components/HeaderNav";
import BoxDiv from "../../containers/BoxDiv";

function Dots(){
    const navigate = useNavigate();
    useEffect(() => {
        let state = AuthorizationStore.getState()
        if(getLogin(state).trim() === "" || getPassword(state).trim() === ""){
            navigate("/");
        }
    })

    return (
        <CoordinatesProvider>
            <HeaderNav/>
            <div className="md:flex">
                <InputFileds/>
                <div className="flex flex-wrap ">
                    <BoxDiv>
                        <Graph width={300} height={300}/>
                    </BoxDiv>
                    <BoxDiv>
                        <DotsTable/>
                    </BoxDiv>
                </div>
            </div>
        </CoordinatesProvider>
    )
}

export default Dots;
