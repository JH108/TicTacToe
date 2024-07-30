# TicTacToe
Built with Kotlin Multiplatform, Ktor, Jetpack Compose, Swift UI, and Kotlin React Wrapper. 

# Status
- [x] Backend Implemented ![](https://progress-bar.dev/100)
- [x] Web Frontend Implemented ![](https://progress-bar.dev/100)
- [x] Android Frontend Implemented ![](https://progress-bar.dev/100)
- [ ] Android Frontend "best practices" ![](https://progress-bar.dev/0)
- [ ] iOS Frontend Implemented ![](https://progress-bar.dev/0)
- [ ] Backend "productionized" ![](https://progress-bar.dev/0)

# Diagrams

## High Level Architecture
```mermaid
flowchart TD
%% Nodes
    subgraph WebApp["Web App"]
        WA("Web Application")
        SKM{{"Shared Models"}}
    end

    subgraph AndroidApp["Android App"]
        AA("Android Application")
        AndroidDB[(Database)]
        SKM1{{"Shared Models"}}
    end

    subgraph iOSApp["iOS App"]
        IA("iOS Application")
        DB[(Database)]
        SKM2{{"Shared Models"}}
    end

    subgraph KtorBackend["KTOR Backend Service"]
        KAPI(["Ktor API"])
        BackendDB[(Database)]
        SKM3{{"Shared Models"}}
        KC(["Ktor Client"])
    end

%% Define Edges
    iOSApp --> KtorBackend
    AndroidApp --> KtorBackend
    WebApp --> KtorBackend
    WA <--> KC
%% Define Styling
    style WA fill: #FF69B4, stroke: #FF69B4, color: black
    style AA fill: #00C853, stroke: #00C853, color: black
    style IA fill: #2962FF, stroke: #2962FF
    style SKM fill: #FFA500, stroke: #FFA500, color: #000
    style SKM1 fill: #FFA500, stroke: #FFA500, color: #000
    style SKM2 fill: #FFA500, stroke: #FFA500, color: #000
    style SKM3 fill: #FFA500, stroke: #FFA500, color: #000
    style KAPI fill: #777, stroke: #000
    style KC fill: purple
    style BackendDB fill: #FFA500, stroke: #FFA500, color: black
    style DB fill: #FFA500, stroke: #FFA500, color: black
    style AndroidDB fill: #FFA500, stroke: #FFA500, color: black
```