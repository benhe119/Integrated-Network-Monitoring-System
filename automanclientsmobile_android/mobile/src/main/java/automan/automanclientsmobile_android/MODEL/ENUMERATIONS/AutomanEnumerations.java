package automan.automanclientsmobile_android.MODEL.ENUMERATIONS;

/**
 * Created by F on 5/7/2017.
 */
public enum AutomanEnumerations {
    // AUTOMAN MOBILE APP STATES
    MYDATA, DATABASE, DASHBOARD, SETTINGS, AUTO_SECURITY,
    // AUTOMAN MYDATA STATES
    MYDATA_EMPLOYEE, MYDATA_CLIENT, MYDATA_STAKEHOLDER, MYDATA_USER,
    // AUTOMAN AUTO_SECURITY STATES
    AUTO_AUDITING,

    //  AUTOMAN - PROFILE DATA
    AUTOMAN, autoassistants, 

    // AUTOMAN API - COMPANY DATA
    autoobjects, 
    users, projects, tasks, companies, departments, employees, clients, stakeholders,
    meetings, performances, positions, privileges, securitylogins, datasecurities,
    accounts, banks,   
    requests, complaints, enquiries,

    // CONFIG DATA
    settings, autoactions, autodata,

    //   AUTO-AUDITING
    autoaudits, autologs, autoevents,

    //  PUBLIC DATA
    jobs, resumes, applicants,
    products, services, customers,
    stocks, investors,

    //  AUTOMAN CLIENTS - CONTACT METHODS
    sms, email, cemail, call;

    public static AutomanEnumerations toAutoEnum(String sth){
        switch(sth){
            case "AUTOMAN":
                return AUTOMAN;
            case "Auto-Assistant":
                return autoassistants;
            case "autoobject":
                return autoobjects;
            case "user":
                return users;
            case "project":
                return projects;
            case "task":
                return tasks;
            case "company":
                return companies;
            case "department":
                return departments;
            case "employee":
                return employees;
            case "client":
                return clients;
            case "stakeholder":
                return stakeholders;
            case "meeting":
                return meetings;
            case "performance":
                return performances;
            case "position":
                return positions;
            case "privilege":
                return privileges;
            case "securitylogin":
                return securitylogins;
            case "datasecurity":
                return datasecurities;
            case "account":
                return accounts;
            case "bank":
                return banks;
            case "request":
                return requests;
            case "complaint":
                return complaints;
            case "enquiry":
                return enquiries;
            //  CONFIG DATA
            case "setting":
                return settings;
            case "autoaction":
                return autoactions;
            case "autodata":
                return autodata;
            case "autoaudit":
                return autoaudits;
            case "autolog":
                return autologs;
            case "autoevent":
                return autoevents;
            //  PUBLIC DATA
            case "job":
                return jobs;
            case "resume":
                return resumes;
            case "applicant":
                return applicants;
            case "product":
                return products;
            case "service":
                return services;
            case "customer":
                return customers;
            case "stock":
                return stocks;
            case "investor":
                return investors;
            default :
                return null;

        }
    }

    public String toString(){
        return this.toString(this);
    }

    public String toString(AutomanEnumerations sth){
        switch(sth){
            case AUTOMAN:
                return "AUTOMAN";
            case autoassistants:
                return "Auto-Assistant";
            case autoobjects:
                return "autoobjects";
            case users:
                return "users";
            case projects:
                return "projects";
            case tasks:
                return "tasks";
            case companies:
                return "companies";
            case departments:
                return "departments";
            case employees:
                return "employees";
            case clients:
                return "clients";
            case stakeholders:
                return "stakeholders";
            case meetings:
                return "meetings";
            case performances:
                return "performances";
            case positions:
                return "positions";
            case privileges:
                return "privileges";
            case securitylogins:
                return "securitylogins";
            case datasecurities:
                return "datasecurities";
            case accounts:
                return "accounts";
            case banks:
                return "banks";
            case requests:
                return "requests";
            case complaints:
                return "complaints";
            case enquiries:
                return "enquiries";
            //  CONFIG DATA
            case settings:
                return "settings";
            case autoactions:
                return "autoactions";
            case autodata:
                return "autodata";
            case autoaudits:
                return "autoaudits";
            case autologs:
                return "autologs";
            case autoevents:
                return "autoevents";
            //  PUBLIC DATA
            case jobs:
                return "jobs";
            case resumes:
                return "resumes";
            case applicants:
                return "applicants";
            case products:
                return "products";
            case services:
                return "services";
            case customers:
                return "customers";
            case stocks:
                return "stocks";
            case investors:
                return "investors";
            default:
                return "";

        }
    }

    public String toSingularLowerCase(AutomanEnumerations sth){
        String str = this.toString(sth);
        str = str.trim().endsWith("ies") ? str.replaceFirst("ies", "ys") : str;
        return str.substring(0, str.lastIndexOf("s"));
    }

    public String toSingularUpperCase(AutomanEnumerations sth){
        return this.toSingularLowerCase(sth).toUpperCase();
    }

    public String toPluralUpperCase(AutomanEnumerations sth){
        return this.toString(sth).toUpperCase();
    }

    public String toSingularCamelCase(AutomanEnumerations sth){
        if(sth == stakeholders) return "StakeHolder";
        if(sth == securitylogins) return "SecurityLogin";
        if(sth == autologs) return "AutoLog";
        if(sth == autoevents) return "AutoEvent";
        String str = this.toSingularLowerCase(sth);
        return str.replaceFirst((str.charAt(0)+""), (str.charAt(0)+"").toUpperCase());
    }

    public String toPluralCamelCase(AutomanEnumerations sth){
        if(sth == stakeholders) return "StakeHolders";
        if(sth == securitylogins) return "SecurityLogins";
        if(sth == autologs) return "AutoLogs";
        if(sth == autoevents) return "AutoEvents";
        String str = this.toString(sth);
        return str.replaceFirst((str.charAt(0)+""), (str.charAt(0)+"").toUpperCase());
    }
}

