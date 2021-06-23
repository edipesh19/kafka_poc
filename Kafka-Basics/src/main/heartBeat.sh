#/bin/bash
host=$1
generate_hb_data()
{
  cat <<EOF
  {
    "timestamp":0,
    "agentOverallHealth":true,
    "jobStats":{},
    "health":
    {"echo":[{"name":"echo","agentTaskRunningSuccess":true,"lastRunTimeMillies":0,"lastTaskRunTimeTakenMillies":0,"lastKnownException":null,"details":null,"lastRunTimeDisplayString":"01-Jan-1970 00:00:00 GMT"}],
    "agent-plugin":[{"name":"agent-plugin","agentTaskRunningSuccess":true,"lastRunTimeMillies":0,"lastTaskRunTimeTakenMillies":0,"lastKnownException":null,"details":null,"lastRunTimeDisplayString":"01-Jan-1970 00:00:00 GMT"}]
  }
}
EOF
}
while [ true ]
do
    op=`curl -s \
    -H "di-internal-request:abcd" \
    -H "Content-Type:application/json" \
    -X POST --data "$(generate_hb_data)" \
    http://${host}:8080/20200430/agents/58c9305b-31d9-47f1-ae20-77ff72213332/heartbeat`
    echo "$op"
    sleep .5
done