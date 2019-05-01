package net.tommyc.android.tourofhonor;

/*
class AppDataDBAdapter extends BaseAdapter {

    private Context context;
    private List<Object> objects;

    public AppDataDBAdapter(Context context, List<Object> objects) {
        this.context = context;
        this.objects = objects;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView==null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(bonusListing).inflate(android.R.layout.bonus_list_row_item, parent, false);
            holder.text = convertView.findViewById(android.R.id.bonusCode);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.text.setText(getItem(position).toString());
        return convertView;
    }

    class ViewHolder {
        TextView text;
    }
}
*/

