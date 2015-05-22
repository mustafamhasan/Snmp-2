package snmp_commands;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;

/**
 * Created by fnovikov on 22.05.2015.
 */
public class SNMPGet {
    public static void main(String[] args) throws IOException {
        // TODO:
        // snmpGet()
    }

    public String snmpGet(String host, String community, String strOID) {
        String strResponse="";
        ResponseEvent response;
        Snmp snmp;
        try {
            OctetString community1 = new OctetString(community);
            host= host+"/"+"161";
            Address tHost = new UdpAddress(host);
            TransportMapping transport = new DefaultUdpTransportMapping();
            transport.listen();
            CommunityTarget comtarget = new CommunityTarget();
            comtarget.setCommunity(community1);
            comtarget.setVersion(SnmpConstants.version1);
            comtarget.setAddress(tHost);
            comtarget.setRetries(2);
            comtarget.setTimeout(5000);
            PDU pdu = new PDU();
            pdu.add(new VariableBinding(new OID(strOID)));
            pdu.setType(PDU.GET);
            snmp = new Snmp(transport);
            response = snmp.get(pdu,comtarget);
            if(response != null) {
                if(response.getResponse().getErrorStatusText().equalsIgnoreCase("Success")) {
                    PDU pduresponse=response.getResponse();
                    strResponse=pduresponse.getVariableBindings().firstElement().toString();
                    if(strResponse.contains("=")) {
                        int len = strResponse.indexOf("=");
                        strResponse=strResponse.substring(len+1, strResponse.length());
                    }
                }
            } else {
                System.out.println("Looks like a TimeOut occured ");
            }
            snmp.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
//System.out.println("Response="+strResponse);
        return strResponse;
    }
}
